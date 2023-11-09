package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.service.IoTService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iot")
public class IoTController {

    private final IoTService iotService;

    public IoTController(IoTService iotService) {
        this.iotService = iotService;
    }

    @PostMapping("polls/{poll-id}")
    public ResponseEntity<String> connectToPoll(@PathVariable("poll-id") Long id) {
        String token = iotService.addIotToPoll(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return ResponseEntity.ok().headers(headers).body(token);
    }

    @PostMapping("polls/{poll-id}/votes")
    @PreAuthorize("@authService.iotCanVote(#id)")
    public void voteOnPoll(@PathVariable("poll-id") Long id, @RequestBody List<VoteDTO> votes) {
        iotService.vote(id, votes);
    }
}
