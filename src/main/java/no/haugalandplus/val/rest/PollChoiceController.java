package no.haugalandplus.val.rest;

import no.haugalandplus.val.repository.PollChoiceRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PollChoiceController {

    private PollChoiceRepository pollChoiceRepository;
}
