package no.haugalandplus.val.repository;

import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class PollRepositoryTest extends TestUtils {

    @Autowired
    private PollRepository pollRepository;

    @Test
    @Transactional
    void existsByPollIdAndUser() {
        User user = saveNewUser();
        Poll poll = saveNewPoll(user);
        assertThat(pollRepository.existsByPollIdAndUser(poll.getPollId(), user), is(true));
    }

//    @Test
//    @Transactional
//    void existsByPollIdAndIotListContainingAndStatus() {
//    }
}