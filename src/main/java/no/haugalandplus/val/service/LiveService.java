package no.haugalandplus.val.service;

import lombok.Getter;
import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.StartPollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LiveService {
    private final long delay = 1000;
    private final Map<Long, PollHandler> pollMap = new ConcurrentHashMap<>();
    private final PollService pollService;

    public LiveService(PollService pollService) {
        this.pollService = pollService;
    }

    public Sinks.Many<PollDTO> getPollSink(Long pollId) {
        PollHandler poll = pollMap.computeIfAbsent(pollId, k -> new PollHandler(pollId, Sinks.many().multicast().onBackpressureBuffer()));
        return poll.getPollSinks();
    }

    public void sendUpdateToPoll(Long pollId) {
        PollHandler poll = pollMap.get(pollId);
        if (poll != null) {
            poll.sendUpdate();
        }
    }

    public PollDTO startPoll(Long pollId, StartPollDTO startPollDTO) {
        PollDTO pollDTO = pollService.start(pollId, startPollDTO);
        sendUpdateToPoll(pollId);
        return pollDTO;
    }

    public PollDTO endPoll(Long pollId) {
        PollDTO pollDTO = pollService.end(pollId);
        sendUpdateToPoll(pollId);
        return pollDTO;
    }

    public void vote(Long id, VoteDTO vote) {
        pollService.vote(id, vote);
        sendUpdateToPoll(id);
    }

    @Scheduled(fixedRate = 10*60*1000)
    public void cleanupExpiredPolls() {
        pollMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    private class PollHandler {
        private final Long pollId;
        @Getter
        private final Sinks.Many<PollDTO> pollSinks;
        private final AtomicBoolean scheduled;
        private final AtomicLong lastEvent;

        private PollHandler(Long pollId, Sinks.Many<PollDTO> pollSinks) {
            this.pollId = pollId;
            this.pollSinks = pollSinks;
            this.scheduled = new AtomicBoolean(false);
            this.lastEvent = new AtomicLong(0);
        }

        public void sendUpdate() {
            synchronized (lastEvent) {
                Long dif = System.currentTimeMillis() - lastEvent.get();
                if (dif >= delay) {
                    sendEvent();
                }
                if (scheduled.compareAndExchange(false, true)) {
                    scheduledEvent(dif);
                }
            }
        }

        private void scheduledEvent(Long dif) {
            lastEvent.set(System.currentTimeMillis() + delay);
            Mono.delay(Duration.ofMillis(dif)).subscribe(t -> {
                sendEvent();
                scheduled.set(false);
            });
        }

        private void sendEvent() {
            lastEvent.set(System.currentTimeMillis());
            PollDTO poll = pollService.updatePollResult(pollId);
            pollSinks.tryEmitNext(poll);
            if (poll.getStatus() == PollStatusEnum.ENDED) {
                pollSinks.tryEmitComplete();
            }
        }

        public boolean isEmpty() {
            return pollSinks.currentSubscriberCount() == 0;
        }
    }
}
