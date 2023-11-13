package no.haugalandplus.val;

import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import no.haugalandplus.val.service.RoomCodeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@ActiveProfiles("test")
public class TestUtils {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PollRepository pollRepository;

    private final Random random = new Random(42);

    @AfterEach
    public void clear() {
        SecurityContextHolder.clearContext();
    }

    public User saveNewUser() {
        User user = new User();
        user.setUsername("Guro Victoria" + random.nextInt());
        user.setPassword("Dette er et rop om help");
        return saveNewUser(user);
    }

    public User saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void setSecurityContextUser() {
        setSecurityContextUser(saveNewUser());
    }

    public void setSecurityContextUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()));
    }

    public Poll saveNewPoll() {
        User u = saveNewUser();
        return saveNewPoll(u);
    }

    public Poll saveNewPoll(User u) {
        Poll poll = new Poll();
        poll.setUser(u);
        poll.setNeedLogin(false);
        poll.setChoices(new ArrayList<>());
        poll.setTitle("Is this a good question?");
        poll.setDescription("Lalala");
        poll.setStatus(PollStatusEnum.NOT_INITIALISED);
        poll = pollRepository.save(poll);
        poll.setRoomCode(RoomCodeHelper.generateRoomCode(poll.getPollId()));
        return pollRepository.save(poll);
    }

    public Poll addChoices(Poll poll) {
        List<Choice> choiceList = new ArrayList<>();
        Choice choice1 = new Choice();
        choice1.setTitle("Yes");
        choice1.setDescription("No");
        poll.addChoice(choice1);

        Choice choice2 = new Choice();
        choice2.setTitle("No");
        choice2.setDescription("Yes");
        poll.addChoice(choice2);

        poll = pollRepository.save(poll);
        assertThat(poll, notNullValue());
        assertThat(poll.getChoices(), hasSize(2));

        assertThat(poll.getChoices().get(0).getPoll(), notNullValue());

        return poll;
    }

    public Poll startPoll(Poll poll) {
        poll.setStatus(PollStatusEnum.ACTIVE);
        poll.setStartTime(new Date());
        return pollRepository.save(poll);
    }
}
