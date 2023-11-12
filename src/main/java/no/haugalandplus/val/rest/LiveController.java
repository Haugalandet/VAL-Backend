package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.service.LiveService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
public class LiveController {

    private final LiveService liveService;

    public LiveController(LiveService liveService) {
        this.liveService = liveService;
    }

    @GetMapping(value = "/polls/{id}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<PollDTO>> pollSse(@PathVariable Long id) {
        Sinks.Many<PollDTO> pollSink = liveService.getPollSink(id);
        return pollSink.asFlux().map(data -> ServerSentEvent.builder(data).build());
    }
}
