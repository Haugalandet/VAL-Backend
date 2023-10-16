package no.haugalandplus.val.service;

import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.dto.ChoiceDTO;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.ChoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class PollServiceTest extends TestUtils {

    @Autowired
    private PollService pollService;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Test
    @Transactional
    public void testInsertion() {
        User user = saveNewUser();
        setSecurityContextUser(user);
        PollDTO pollDTO = new PollDTO();
        pollDTO.setName("Noting?");
        pollDTO.setDescription("Wow, very cool question");

        List<ChoiceDTO> choices = new ArrayList<>();

        ChoiceDTO choiceDTO = new ChoiceDTO();
        choiceDTO.setName("yes");
        choiceDTO.setDescription("No");
        choices.add(choiceDTO);

        choiceDTO = new ChoiceDTO();
        choiceDTO.setName("no");
        choiceDTO.setDescription("yes");
        choices.add(choiceDTO);

        pollDTO.setChoices(choices);

        PollDTO poll = pollService.createPoll(pollDTO);
        assertThat(poll.getChoices(), hasSize(2));

        pollDTO = new PollDTO();
        pollDTO.setName("This is a super good test?");
        pollDTO.setDescription("NOthing is wrong?");

        choices = new ArrayList<>();

        choiceDTO = new ChoiceDTO();
        choiceDTO.setName("Sure");
        choiceDTO.setDescription("No");
        choices.add(choiceDTO);

        choiceDTO = new ChoiceDTO();
        choiceDTO.setName("Okay");
        choiceDTO.setDescription(":)");
        choices.add(choiceDTO);

        pollDTO.setChoices(choices);

        poll = pollService.createPoll(pollDTO);

        assertThat(pollService.getAllPolls(), hasSize(2));

        assertThat(pollService.getPoll(poll.getPollId()), notNullValue());
        assertThat(pollService.getPoll(poll.getPollId()).getChoices(), hasSize(2));
        ChoiceDTO c1 = pollService.getPoll(poll.getPollId()).getChoices().get(0);
        assertThat(c1.getName(), is("Sure"));

    }
}