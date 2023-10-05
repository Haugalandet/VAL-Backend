package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.PollInstDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.service.PollInstService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/poll-instances", "/poll/{poll-id}/poll-instances"})
public class PollInstController {

    private PollInstService pollInstService;

    public PollInstController(PollInstService pollInstService) {
        this.pollInstService = pollInstService;
    }

    @GetMapping
    public List<PollInstDTO> getAllPollInsts() {
        return pollInstService.getAllPollInsts();
    }

    @GetMapping("/{id}")
    public PollInstDTO getPollInst(@PathVariable Long id) {
        return pollInstService.getPollInst(id);
    }

    @PostMapping
    @PutMapping("/{id}")
    public PollInstDTO insertOrUpdatePollInst(@RequestBody PollInstDTO pollInstDTO) {
        return pollInstService.save(pollInstDTO);
    }

    @DeleteMapping("/{id}")
    public PollInstDTO deletePollInst(@PathVariable Long id) {
        return pollInstService.deletePollInst(id);
    }

    @PostMapping("/{pollInstId}/votes")
    public boolean vote(@PathVariable Long pollInstId, @RequestBody VoteDTO vote) {
        return pollInstService.vote(pollInstId, vote);
    }
}
