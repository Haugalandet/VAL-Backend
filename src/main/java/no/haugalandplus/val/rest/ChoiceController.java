//package no.haugalandplus.val.rest;
//
//import no.haugalandplus.val.dto.ChoiceDTO;
//import no.haugalandplus.val.service.ChoiceService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/polls/{poll-id}/choices")
//public class ChoiceController {
//
//    private ChoiceService choiceService;
//
//    public ChoiceController(ChoiceService choiceService) {
//        this.choiceService = choiceService;
//    }
//
//    @GetMapping
//    public List<ChoiceDTO> getAllPollChoices() {
//        return choiceService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public ChoiceDTO getPollChoice(@PathVariable Long id) {
//        return choiceService.get(id);
//    }
//
//    @PostMapping
//    @PutMapping("/{id}")
//    public ChoiceDTO createPollChoice(@RequestBody ChoiceDTO pollChoice, @PathVariable("poll-id") Long pollId) {
//        return choiceService.saveOrUpdate(pollId, pollChoice);
//    }
//
//    @DeleteMapping("/{id}")
//    public ChoiceDTO deletePollChoice(@PathVariable Long id) {
//        return choiceService.delete(id);
//    }
//}
