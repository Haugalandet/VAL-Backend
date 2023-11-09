package no.haugalandplus.val.service;

import no.haugalandplus.val.auth.JwtTokenUtil;
import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.constants.UserTypeEnum;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.entities.Vote;
import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class IoTService extends ServiceUtils {

    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ChoiceRepository choiceRepository;
    private final VoteRepository voteRepository;

    public IoTService(PollRepository pollRepository, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, ChoiceRepository choiceRepository, VoteRepository voteRepository) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.choiceRepository = choiceRepository;
        this.voteRepository = voteRepository;
    }

    public String addIotToPoll(Long pollId) {
        Poll poll = pollRepository.findById(pollId).get();
        if (poll.getStatus() != PollStatusEnum.NOT_INITIALISED) {
            throw new RuntimeException("Can not add IoT-device to initiated polls");
        }

        User iot = new User();
        iot.setUsername(UUID.randomUUID().toString());
        iot.setUserType(UserTypeEnum.IOT);
        iot = userRepository.save(iot);

        poll.getIotList().add(iot);

        pollRepository.save(poll);

        return jwtTokenUtil.createJWT(iot.getUserId());
    }

    public Poll removeIot(Poll poll) {
        List<User> iotList = poll.getIotList();
        poll.setIotList(new ArrayList<>());
        pollRepository.save(poll);
        userRepository.deleteAll(
                iotList.stream()
                    .filter(iot -> iot.getUserType() == UserTypeEnum.IOT)
                    .toList()
        );
        return poll;
    }

    public void vote(Long id, List<VoteDTO> votes) {
        List<Vote> voteList = new ArrayList<>();
        votes.forEach( voteDTO -> {
            Choice choice = choiceRepository.findById(voteDTO.getChoiceId()).get();
            if (!Objects.equals(choice.getPoll().getPollId(), id)) {
                throw new RuntimeException("Choice does not belong to poll");
            }
            Vote v = new Vote();
            v.setVoteCount(voteDTO.getVoteCount().intValue());
            v.setChoice(choice);
            v.setVoter(getCurrentUser());
            voteList.add(v);
        });
        voteRepository.saveAll(voteList);
    }
}
