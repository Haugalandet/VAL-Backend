package no.haugalandplus.val.service;

import io.jsonwebtoken.Claims;
import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.auth.JwtTokenUtil;
import no.haugalandplus.val.constants.UserTypeEnum;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.entities.Vote;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class IoTServiceTest extends TestUtils {

    @Autowired
    private IoTService ioTService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PollService pollService;

    @Autowired
    private VoteRepository voteRepository;


    @Test
    @Transactional
    public void addIoT() {
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        String token = ioTService.addIotToPoll(poll.getPollId());

        Claims claims = jwtTokenUtil.isExpired(token);

        User iot = userRepository.findById(Long.parseLong(claims.getSubject())).get();

        assertThat(iot.getUserType(), is(UserTypeEnum.IOT));

        poll = pollRepository.findById(poll.getPollId()).get();

        assertThat(poll.getIotList(), hasSize(1));
        assertThat(poll.getIotList().get(0).getUserId(), is(iot.getUserId()));

        startPoll(poll);

        try {
            ioTService.addIotToPoll(poll.getPollId());
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }
    }

    @Test
    @Transactional
    public void addIoTVote() {
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        String token = ioTService.addIotToPoll(poll.getPollId());

        Claims claims = jwtTokenUtil.isExpired(token);

        User iot = userRepository.findById(Long.parseLong(claims.getSubject())).get();

        setSecurityContextUser(iot);

        poll = startPoll(poll);

        VoteDTO vote = new VoteDTO();
        vote.setChoiceId(poll.getChoices().get(0).getChoiceId());
        vote.setVoteCount(100L);

        ioTService.vote(poll.getPollId(), List.of(vote));

        List<Vote> votes = voteRepository.findAll();
        assertThat(votes, hasSize(1));
        assertThat(votes.get(0).getVoteCount(), is(100));
    }

    @Test
    @Transactional
    public void testRemoveAll() {
        Poll poll = saveNewPoll();
        poll = addChoices(poll);

        Long nrUsers = userRepository.count();

        String token = ioTService.addIotToPoll(poll.getPollId());

        assertThat(userRepository.count(), is(nrUsers+1));

        Claims claims = jwtTokenUtil.isExpired(token);
        User iot = userRepository.findById(Long.parseLong(claims.getSubject())).get();

        ioTService.removeIot(pollRepository.findById(poll.getPollId()).get());

        assertThat(userRepository.count(), is(nrUsers));
        assertThat(userRepository.existsByUsername(iot.getUsername()), is(false));
    }
}