package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.PollChoiceDTO;
import no.haugalandplus.val.service.PollChoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/poll/{pollid}/choices")
public class PollChoiceController {

    private PollChoiceService pollChoiceService;

    public PollChoiceController(PollChoiceService pollChoiceService) {
        this.pollChoiceService = pollChoiceService;
    }

    @GetMapping
    public List<PollChoiceDTO> getAllPollChoices() {
        return pollChoiceService.getAll();
    }

    @GetMapping("/{id}")
    public PollChoiceDTO getPollChoice(@PathVariable Long id) {
        return pollChoiceService.get(id);
    }

    @PostMapping
    @PutMapping("/{id}")
    public PollChoiceDTO createPollChoice(@RequestBody PollChoiceDTO pollChoice, @PathVariable("pollid") Long pollId) {
        return pollChoiceService.saveOrUpdate(pollId, pollChoice);
    }

    @DeleteMapping("/{id}")
    public PollChoiceDTO deletePollChoice(@PathVariable Long id) {
        return pollChoiceService.delete(id);
    }
}
