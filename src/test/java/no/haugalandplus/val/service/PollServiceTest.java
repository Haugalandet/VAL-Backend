package no.haugalandplus.val.service;

import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.dto.ChoiceDTO;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.StartPollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.mock.ClockMock;
import no.haugalandplus.val.mock.PollServiceMock;
import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.VoteRepository;
import no.haugalandplus.val.service.poll.PollService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
class PollServiceTest extends TestUtils {

    @Autowired
    private PollService pollService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private LiveService liveService;

    @Autowired
    private ClockMock clockMock;

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

        pollService.start(poll.getPollId(), new StartPollDTO());

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

        pollService.start(poll.getPollId(), new StartPollDTO());

        PollDTO pollDTO = pollService.getPoll(poll.getPollId());

        assertThat(pollDTO.getPollId(), is(poll.getPollId()));
        assertThat(pollDTO.getChoices(), hasSize(2));
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

        pollService.start(poll.getPollId(), new StartPollDTO());

        try {
            pollService.start(poll.getPollId(), null);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }

        pollService.end(poll.getPollId());

        try {
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

        poll.setStartTime(new Date(16440544000000L));
        poll = pollRepository.save(poll);

        liveService.pollChange();

        PollDTO pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.NOT_INITIALISED));

        poll.setStartTime(new Date(1644054400000L));
        poll = pollRepository.save(poll);

        liveService.pollChange();

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ACTIVE));

        poll.setEndTime(new Date(16440544000000L));
        poll = pollRepository.save(poll);

        liveService.pollChange();

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ACTIVE));

        poll.setEndTime(new Date(1644054400000L));
        poll = pollRepository.save(poll);

        liveService.pollChange();

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ENDED));
    }

    @Test
    @Transactional
    public void roomCode() {
        Poll poll = saveNewPoll();
        assertThat(poll.getRoomCode(), notNullValue());

        startPoll(poll);

        PollDTO pollDTO = pollService.getPollWithRoomCode(poll.getRoomCode());

        assertThat(pollDTO.getPollId(), is(poll.getPollId()));
    }

    @Test
    @Transactional
    public void startDeleteVotesOnRestart() {
        User user = saveNewUser();
        Poll poll = saveNewPoll(user);
        poll = addChoices(poll);

        setSecurityContextUser(user);

        pollService.start(poll.getPollId(), new StartPollDTO());

        VoteDTO v = new VoteDTO();
        v.setChoiceId(poll.getChoices().get(0).getChoiceId());

        pollService.vote(poll.getPollId(), v);
        pollService.vote(poll.getPollId(), v);

        PollDTO pollDTO = pollService.updatePollResult(poll.getPollId());

        assertThat(voteRepository.count(), is(2L));
        assertThat(pollDTO.getChoices().get(0).getVoteCount(), is(2L));

        pollDTO = pollService.end(pollDTO.getPollId());

        assertThat(voteRepository.count(), is(2L));
        assertThat(pollDTO.getChoices().get(0).getVoteCount(), is(2L));

        pollDTO = pollService.start(pollDTO.getPollId(), new StartPollDTO());

        assertThat(voteRepository.count(), is(0L));
        assertThat(pollDTO.getChoices().get(0).getVoteCount(), is(0L));
    }

    @Test
    @Transactional
    public void startNowTimedEnd() {
        User user = saveNewUser();
        Poll poll = saveNewPoll(user);
        poll = addChoices(poll);

        setSecurityContextUser(user);

        long time = 10000000L;

        clockMock.setCurrentDate(new Date(time));

        StartPollDTO startPollDTO = new StartPollDTO();
        startPollDTO.setEndTime(new Date(time + 1000));

        pollService.start(poll.getPollId(), startPollDTO);

        liveService.pollChange();

        poll = pollRepository.findById(poll.getPollId()).get();

        assertThat(poll.getStatus(), is(PollStatusEnum.ACTIVE));
        assertThat(poll.getStartTime(), is(new Date(time)));

        clockMock.setCurrentDate(new Date(time + 100000));

        liveService.pollChange();

        PollDTO pollDTO = pollService.getPoll(poll.getPollId());

        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ENDED));
    }

    @Test
    @Transactional
    public void timedStartAndEnd() {
        User user = saveNewUser();
        Poll poll = saveNewPoll(user);
        poll = addChoices(poll);

        setSecurityContextUser(user);

        long time = 10000000L;

        clockMock.setCurrentDate(new Date(time));

        StartPollDTO startPollDTO = new StartPollDTO();
        startPollDTO.setEndTime(new Date(time + 1000));
        startPollDTO.setStartTime(new Date(time + 100));

        pollService.start(poll.getPollId(), startPollDTO);

        liveService.pollChange();

        PollDTO pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.NOT_INITIALISED));

        clockMock.setCurrentDate(new Date(time + 200));

        liveService.pollChange();

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ACTIVE));

        clockMock.setCurrentDate(new Date(time + 100000));

        liveService.pollChange();

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getStatus(), is(PollStatusEnum.ENDED));
    }

    @Test
    @Transactional
    public void hasVotedTest() {
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        PollDTO pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getHasUserVoted(), nullValue());

        User user = saveNewUser();
        setSecurityContextUser(user);

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getHasUserVoted(), is(false));

        startPoll(poll);

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setChoiceId(poll.getChoices().get(0).getChoiceId());
        pollService.vote(pollDTO.getPollId(), voteDTO);

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getHasUserVoted(), is(true));
    }

    @Test
    @Transactional
    public void deletePoll() {
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        PollDTO pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getHasUserVoted(), nullValue());

        User user = saveNewUser();
        setSecurityContextUser(user);

        pollDTO = pollService.getPoll(poll.getPollId());
        assertThat(pollDTO.getHasUserVoted(), is(false));

        startPoll(poll);

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setChoiceId(poll.getChoices().get(0).getChoiceId());
        pollService.vote(pollDTO.getPollId(), voteDTO);

        assertThat(voteRepository.count(), is(1L));

        pollService.deletePoll(poll.getPollId());

        assertThat(voteRepository.count(), is(0L));
        assertThat(choiceRepository.count(), is(0L));
        assertThat(pollRepository.count(), is(0L));

    }
}