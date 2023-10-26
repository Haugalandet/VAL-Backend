package no.haugalandplus.val.service;

import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.dto.PollInstDTO;
import no.haugalandplus.val.dto.PollResultDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.PollInst;
import no.haugalandplus.val.repository.PollInstRepository;
import no.haugalandplus.val.repository.PollResultRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class PollInstServiceTest extends TestUtils {

    @Autowired
    private PollInstService pollInstService;

    @Autowired
    private PollResultRepository pollResultRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @Transactional
    public void createInst() {
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        PollInstDTO pollInstDTO = new PollInstDTO();
        pollInstDTO.setName("Ok");

        PollInstDTO pollInst = pollInstService.insertPollInst(poll.getPollId(), pollInstDTO);

        assertThat(pollInst.getPollId(), is(poll.getPollId()));
        assertThat(pollInst.getPollResult(), hasSize(2));
        assertThat(pollInst.getPollResult().get(0).getPollChoice(), notNullValue());

        assertThat(pollResultRepository.findAll(), hasSize(2));
    }

    @Test
    @Transactional
    public void voteTest() {
        setSecurityContextUser();
        Poll poll = saveNewPoll();
        poll = addChoices(poll);
        PollInst pollInst = createPollInst(poll);

        Choice choice = poll.getChoices().get(0);

        VoteDTO vote = new VoteDTO();
        vote.setChoiceId(choice.getChoiceId());

        pollInstService.vote(pollInst.getPollInstId(), vote);

        assertThat(voteRepository.findAll(), hasSize(1));

        PollInstDTO pollInstDTO = pollInstService.getPollInst(pollInst.getPollInstId());
        assertThat(pollInstDTO.getPollResult(), hasSize(2));
        PollResultDTO resultDTO = pollInstDTO.getPollResult().get(0);

        assertThat(resultDTO.getPollChoice(), notNullValue());
        assertThat(resultDTO.getPollChoice().getChoiceId(), is(choice.getChoiceId()));
        assertThat(resultDTO.getTotalCount(), is(1L));
    }
}