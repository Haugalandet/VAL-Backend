package no.haugalandplus.val.service;

import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.StartPollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LiveService {
    private final long delay = 1000;
    private final Map<Long, PollHandler> pollMap = new ConcurrentHashMap<>();
    private final PollService pollService;

    private final IoTService iotService;

    public LiveService(PollService pollService, IoTService iotService) {
        this.pollService = pollService;
        this.iotService = iotService;
    }

    public Sinks.Many<PollDTO> getPollSink(Long pollId) {
        return getPollHandler(pollId).getPollSinks();
    }

    private PollHandler getPollHandler(Long pollId) {
        return pollMap.computeIfAbsent(pollId, k -> new PollHandler(pollId));
    }

    public void sendUpdateToPoll(Long pollId) {
        PollHandler poll = getPollHandler(pollId);
        poll.sendUpdate();
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

    public void iotVote(Long id, List<VoteDTO> votes) {
        iotService.vote(id, votes);
        sendUpdateToPoll(id);
    }


    @Scheduled(fixedRate = 10*60*1000)
    public void cleanupExpiredPolls() {
        pollMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }


    private class PollHandler {
        private final Long pollId;
        private Sinks.Many<PollDTO> pollSinks;
        private final AtomicBoolean scheduled;
        private final AtomicLong lastEvent;

        private PollHandler(Long pollId) {
            this.pollId = pollId;
            this.scheduled = new AtomicBoolean(false);
            this.lastEvent = new AtomicLong(0);
        }

        public void sendUpdate() {
            synchronized (lastEvent) {
                long dif = System.currentTimeMillis() - lastEvent.get();
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
            if (pollSinks != null) {
                pollSinks.tryEmitNext(poll);
                if (poll.getStatus() == PollStatusEnum.ENDED) {
                    pollSinks.tryEmitComplete();
                }
            }
        }

        public boolean isEmpty() {
            return pollSinks == null || pollSinks.currentSubscriberCount() == 0;
        }

        public Sinks.Many<PollDTO> getPollSinks() {
            if (pollSinks == null || pollSinks.currentSubscriberCount() == 0) {
                pollSinks = Sinks.many().multicast().onBackpressureBuffer();
            }
            return pollSinks;
        }
    }
}
