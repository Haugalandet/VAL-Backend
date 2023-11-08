package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.service.LiveService;
import no.haugalandplus.val.service.PollService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polls")
public class PollController {
    private final PollService pollService;
    private final LiveService liveService;

    public PollController(PollService pollService, LiveService liveService) {
        this.pollService = pollService;
        this.liveService = liveService;
    }

    @GetMapping
    @PreAuthorize("@authService.isLoggedIn()")
    public List<PollDTO> getAllPollsByCurrentUser() {
        return pollService.getAllPollsByCurrentUser();
    }

    @GetMapping("/{id}")
    public PollDTO getPoll(@PathVariable Long id) {
        return pollService.getPoll(id);
    }

    @PostMapping
    @PreAuthorize("@authService.isLoggedIn()")
    public PollDTO createPoll(@RequestBody PollDTO poll) {
        return pollService.createPoll(poll);
    }

//    @PutMapping("/{id}")
//    public PollDTO updatePoll(@RequestBody PollDTO poll) {
//        return pollService.savePoll(poll);
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authService.ownsPoll(#id)")
    public PollDTO deletePoll(@PathVariable Long id) {
        return pollService.deletePoll(id);
    }

    @PostMapping("/{id}/votes")
    public void vote(@PathVariable Long id, @RequestBody VoteDTO vote) {
        liveService.vote(id, vote);
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("@authService.ownsPoll(#id)")
    public PollDTO startPoll(@PathVariable Long id) {
        return liveService.startPoll(id);
    }

    @PostMapping("/{id}/end")
    @PreAuthorize("@authService.ownsPoll(#id)")
    public PollDTO endPoll(@PathVariable Long id) {
        return liveService.endPoll(id);
    }
}
