package no.haugalandplus.val.service;

import io.jsonwebtoken.Claims;
import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.auth.JwtTokenUtil;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest extends TestUtils {

    @Autowired
    private AuthService authService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollService pollService;

    @Autowired
    private IoTService iotService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void isLoggedInUser() {
        User user = saveNewUser();

        assertThat(authService.isLoggedInUser(user.getUserId()), is(false));

        setSecurityContextUser(saveNewUser());

        assertThat(authService.isLoggedInUser(user.getUserId()), is(false));

        setSecurityContextUser(user);

        assertThat(authService.isLoggedInUser(user.getUserId()), is(true));
    }

    @Test
    @Transactional
    void isLoggedIn() {
        assertThat(authService.isLoggedIn(), is(false));

        User user = saveNewUser();
        setSecurityContextUser(user);

        assertThat(authService.isLoggedIn(), is(true));
    }

    @Test
    @Transactional
    void ownsPoll() {
        User user = saveNewUser();
        Poll poll = saveNewPoll(user);

        assertThat(authService.ownsPoll(poll.getPollId()), is(false));

        setSecurityContextUser(saveNewUser());
        assertThat(authService.ownsPoll(poll.getPollId()), is(false));

        setSecurityContextUser(user);
        assertThat(authService.ownsPoll(poll.getPollId()), is(true));
    }

    @Test
    @Transactional
    void iotCanVote() {
        Poll poll = saveNewPoll();

        String token = iotService.addIotToPoll(poll.getRoomCode());
        Claims claims = jwtTokenUtil.isExpired(token);
        User user = userRepository.findById(Long.parseLong(claims.getSubject())).get();
        setSecurityContextUser(user);

        assertThat(authService.iotCanVote(poll.getPollId()), is(false));

        startPoll(poll);
        assertThat(authService.iotCanVote(poll.getPollId()), is(true));

        token = iotService.addIotToPoll(saveNewPoll().getRoomCode());
        claims = jwtTokenUtil.isExpired(token);
        user = userRepository.findById(Long.parseLong(claims.getSubject())).get();
        setSecurityContextUser(user);

        assertThat(authService.iotCanVote(poll.getPollId()), is(false));
    }

    @Test
    @Transactional
    void canVote() {
        User user = saveNewUser();
        Poll poll = saveNewPoll(user);
        poll = addChoices(poll);

        assertThat(authService.canVote(poll.getChoices().get(0).getChoiceId()), is(false));

        startPoll(poll);

        assertThat(authService.canVote(poll.getChoices().get(0).getChoiceId()), is(true));

        poll.setNeedLogin(true);
        poll = pollRepository.save(poll);

        assertThat(authService.canVote(poll.getChoices().get(0).getChoiceId()), is(false));

        setSecurityContextUser(saveNewUser());
        assertThat(authService.canVote(poll.getChoices().get(0).getChoiceId()), is(true));

        VoteDTO v = new VoteDTO();
        v.setChoiceId(poll.getChoices().get(0).getChoiceId());
        pollService.vote(null, v);

        assertThat(authService.canVote(poll.getChoices().get(0).getChoiceId()), is(false));

        setSecurityContextUser(saveNewUser());
        assertThat(authService.canVote(poll.getChoices().get(0).getChoiceId()), is(true));
    }

    @Test
    @Transactional
    public void iotCanConnect() {
        assertThat(authService.iotCanConnect("Verdens beste poll"), is(false));

        Poll poll = saveNewPoll();
        assertThat(authService.iotCanConnect(poll.getRoomCode()), is(true));

        startPoll(poll);
        assertThat(authService.iotCanConnect(poll.getRoomCode()), is(false));

        assertThat(authService.iotCanConnect(saveNewPoll().getRoomCode()), is(true));

    }
}