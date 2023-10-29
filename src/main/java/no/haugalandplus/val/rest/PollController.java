package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.service.LiveService;
import no.haugalandplus.val.service.PollService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polls")
public class PollController {
    private PollService pollService;
    private LiveService liveService;

    public PollController(PollService pollService, LiveService liveService) {
        this.pollService = pollService;
        this.liveService = liveService;
    }

    @GetMapping
    public List<PollDTO> getAllPollsByCurrentUser() {
        return pollService.getAllPollsByCurrentUser();
    }

    @GetMapping("/{id}")
    public PollDTO getPoll(@PathVariable Long id) {
        return pollService.getPoll(id);
    }

    @PostMapping
    public PollDTO createPoll(@RequestBody PollDTO poll) {
        return pollService.createPoll(poll);
    }

//    @PutMapping("/{id}")
//    public PollDTO updatePoll(@RequestBody PollDTO poll) {
//        return pollService.savePoll(poll);
//    }

    @DeleteMapping("/{id}")
    public PollDTO deletePoll(@PathVariable Long id) {
        return pollService.deletePoll(id);
    }

    @PostMapping("/{id}/votes")
    public void vote(@PathVariable Long id, @RequestBody VoteDTO vote) {
        liveService.vote(id, vote);
    }

    @PostMapping("/{id}/start")
    public PollDTO startPoll(@PathVariable Long id) {
        return liveService.startPoll(id);
    }

    @PostMapping("/{id}/end")
    public PollDTO endPoll(@PathVariable Long id) {
        return liveService.endPoll(id);
    }
}
