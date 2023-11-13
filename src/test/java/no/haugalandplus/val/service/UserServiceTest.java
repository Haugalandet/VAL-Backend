package no.haugalandplus.val.service;

import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest extends TestUtils {

    @Autowired
    private UserService userService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollService pollService;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ChoiceRepository choiceRepository;


    @Test
    @Transactional
    public void deletionTest() {
        User user = saveNewUser();
        Poll poll = saveNewPoll(user);
        poll = addChoices(poll);

        setSecurityContextUser(user);

        startPoll(poll);

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setChoiceId(poll.getChoices().get(0).getChoiceId());
        pollService.vote(poll.getPollId(), voteDTO);

        poll = saveNewPoll();
        poll = addChoices(poll);

        voteDTO = new VoteDTO();
        voteDTO.setChoiceId(poll.getChoices().get(0).getChoiceId());
        pollService.vote(poll.getPollId(), voteDTO);

        assertThat(pollRepository.count(), is(2L));
        assertThat(voteRepository.count(), is(2L));
        assertThat(choiceRepository.count(), is(4L));

        userService.removeUser(user.getUserId());

        assertThat(pollRepository.count(), is(1L));
        assertThat(voteRepository.count(), is(1L));
        assertThat(choiceRepository.count(), is(2L));

    }
}