package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.service.IoTService;
import no.haugalandplus.val.service.LiveService;
import no.haugalandplus.val.service.poll.PollService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iot")
public class IoTController {

    private final IoTService iotService;

    private final LiveService liveService;

    private final PollService pollService;

    public IoTController(IoTService iotService, LiveService liveService, PollService pollService) {
        this.iotService = iotService;
        this.liveService = liveService;
        this.pollService = pollService;
    }

    @PostMapping("polls/{poll-id}")
    @PreAuthorize("@authService.iotCanConnect(#roomCode)")
    public PollDTO connectToPoll(@PathVariable("poll-id") String roomCode) {
        String token = iotService.addIotToPoll(roomCode);
        return pollService.getPollWithRoomCode(roomCode);
    }

    @PostMapping("polls/{poll-id}/votes")
    @PreAuthorize("@authService.iotCanVote(#id)")
    public void voteOnPoll(@PathVariable("poll-id") Long id, @RequestBody List<VoteDTO> votes) {
        liveService.iotVote(id, votes);
    }
}
