package no.haugalandplus.val.repository;

import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class VoteRepositoryTest extends TestUtils {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @Transactional
    public void countVotesTest() {
        User user = saveNewUser();
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        Choice choice1 = poll.getChoices().get(0);
        Choice choice2 = poll.getChoices().get(1);

        Vote vote = new Vote();
        vote.setVoteCount(1);
        vote.setChoice(choice1);

        voteRepository.save(vote);

        Long count = voteRepository.sumOfVotesByChoiceId(choice1.getChoiceId());

        assertThat(count, notNullValue());
        assertThat(count, is(1L));

        count = voteRepository.sumOfVotesByChoiceId(choice2.getChoiceId());

        assertThat(count, notNullValue());
        assertThat(count, is(0L));
    }

}