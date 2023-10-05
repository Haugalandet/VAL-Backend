package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.service.PollService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polls")
public class PollController {

    private PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping
    public List<PollDTO> getAllPolls() {
        return pollService.getAllPolls();
    }

    @GetMapping("/{id}")
    public PollDTO getPoll(@PathVariable Long id) {
        return pollService.getPoll(id);
    }

    @PostMapping
    public PollDTO createPoll(@RequestBody PollDTO poll) {
        return pollService.createPoll(poll);
    }

    @PutMapping("/{id}")
    public PollDTO updatePoll(@RequestBody PollDTO poll) {
        return pollService.savePoll(poll);
    }

    @DeleteMapping("/{id}")
    public PollDTO deletePoll(@PathVariable Long id) {
        return pollService.deletePoll(id);
    }

}
