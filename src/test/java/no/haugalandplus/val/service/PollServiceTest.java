package no.haugalandplus.val.service;

import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.dto.ChoiceDTO;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.entities.Vote;
import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class PollServiceTest extends TestUtils {

    @Autowired
    private PollService pollService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @Transactional
    public void testInsertion() {
        User user = saveNewUser();
        setSecurityContextUser(user);
        PollDTO pollDTO = new PollDTO();
        pollDTO.setTitle("Noting?");
        pollDTO.setDescription("Wow, very cool question");

        List<ChoiceDTO> choices = new ArrayList<>();

        ChoiceDTO choiceDTO = new ChoiceDTO();
        choiceDTO.setTitle("yes");
        choiceDTO.setDescription("No");
        choices.add(choiceDTO);

        choiceDTO = new ChoiceDTO();
        choiceDTO.setTitle("no");
        choiceDTO.setDescription("yes");
        choices.add(choiceDTO);

        pollDTO.setChoices(choices);

        PollDTO poll = pollService.createPoll(pollDTO);
        assertThat(poll.getChoices(), hasSize(2));

        pollDTO = new PollDTO();
        pollDTO.setTitle("This is a super good test?");
        pollDTO.setDescription("NOthing is wrong?");

        choices = new ArrayList<>();

        choiceDTO = new ChoiceDTO();
        choiceDTO.setTitle("Sure");
        choiceDTO.setDescription("No");
        choices.add(choiceDTO);

        choiceDTO = new ChoiceDTO();
        choiceDTO.setTitle("Okay");
        choiceDTO.setDescription(":)");
        choices.add(choiceDTO);

        pollDTO.setChoices(choices);

        poll = pollService.createPoll(pollDTO);

        assertThat(pollService.getAllPolls(), hasSize(2));

        try {
            assertThat(pollService.getPoll(poll.getPollId()), notNullValue());
            fail("The poll is not active, and should not return");
        } catch (Exception e) {
            // Ayyyy
        }

        pollService.start(poll.getPollId());

        assertThat(pollService.getPoll(poll.getPollId()), notNullValue());
        assertThat(pollService.getPoll(poll.getPollId()).getChoices(), hasSize(2));
        ChoiceDTO c1 = pollService.getPoll(poll.getPollId()).getChoices().get(0);
        assertThat(c1.getTitle(), is("Sure"));

    }

    @Test
    @Transactional
    public void createPoll() {
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        pollService.start(poll.getPollId());

        PollDTO pollDTO = pollService.getPoll(poll.getPollId());

        assertThat(pollDTO.getPollId(), is(poll.getPollId()));
        assertThat(pollDTO.getChoices(), hasSize(2));
    }

    @Test
    @Transactional
    public void voteTest() {
        setSecurityContextUser();
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        Choice choice = poll.getChoices().get(0);

        VoteDTO vote = new VoteDTO();
        vote.setChoiceId(choice.getChoiceId());

        try {
            pollService.vote(poll.getPollId(), vote);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }

        pollService.start(poll.getPollId());

        pollService.vote(poll.getPollId(), vote);

        List<Vote> votes = voteRepository.findAll();
        assertThat(votes, hasSize(1));
        assertThat(votes.get(0).getVoteCount(), is(1));

        PollDTO pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getChoices(), hasSize(2));

        ChoiceDTO choiceDTO = pollDTO.getChoices().get(0);

        assertThat(choiceDTO.getVoteCount(), is(0L));

        pollService.updatePollResult(poll.getPollId());

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getChoices(), hasSize(2));

        choiceDTO = pollDTO.getChoices().get(0);

        assertThat(choiceDTO.getVoteCount(), is(1L));

        pollService.end(poll.getPollId());

        try {
            pollService.vote(poll.getPollId(), vote);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }
    }

    @Test
    @Transactional
    public void voteLoginPollTest() {
        setSecurityContextUser();
        Poll poll = saveNewPoll();
        poll = addChoices(poll);
        poll.setNeedLogin(true);
        poll = pollRepository.save(poll);

        pollService.start(poll.getPollId());

        Choice choice = poll.getChoices().get(0);

        VoteDTO vote = new VoteDTO();
        vote.setChoiceId(choice.getChoiceId());

        pollService.vote(poll.getPollId(), vote);

        try {
            pollService.vote(poll.getPollId(), vote);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }
    }

    @Test
    @Transactional
    public void statusChange() {
        setSecurityContextUser();
        Poll poll = saveNewPoll();

        try {
            pollService.end(poll.getPollId());
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }

        pollService.start(poll.getPollId());

        try {
            pollService.start(poll.getPollId());
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }

        pollService.end(poll.getPollId());

        try {
            pollService.start(poll.getPollId());
            fail();
            pollService.end(poll.getPollId());
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }
    }

    @Test
    @Transactional
    public void timedPollTest() {
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        try {
            PollDTO pollDTO = pollService.getPoll(poll.getPollId());
            fail("poll is not stated. Should not return");
        } catch (Exception e) {
            // Eyyy
        }

        poll.setStartTime(new Date(16440544000000L));
        poll = pollRepository.save(poll);

        try {
            PollDTO pollDTO = pollService.getPoll(poll.getPollId());
            fail("poll is not stated. Should not return");
        } catch (Exception e) {
            // Eyyy
        }

        poll.setStartTime(new Date(1644054400000L));
        poll = pollRepository.save(poll);


        PollDTO pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ACTIVE));

        poll.setEndTime(new Date(16440544000000L));
        poll = pollRepository.save(poll);

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ACTIVE));

        poll.setEndTime(new Date(1644054400000L));
        poll = pollRepository.save(poll);

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ENDED));
    }
}