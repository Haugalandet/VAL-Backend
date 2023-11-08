package no.haugalandplus.val.service;

import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.constants.UserTypeEnum;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.entities.Vote;
import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class PollService extends ServiceUtils {

    private final PollRepository pollRepository;
    private final ModelMapper modelMapper;
    private final VoteRepository voteRepository;
    private final ChoiceRepository choiceRepository;
    private final IoTService ioTService;

    public PollService(PollRepository pollRepository, ModelMapper modelMapper, VoteRepository voteRepository, ChoiceRepository choiceRepository, IoTService ioTService) {
        this.pollRepository = pollRepository;
        this.modelMapper = modelMapper;
        this.voteRepository = voteRepository;
        this.choiceRepository = choiceRepository;
        this.ioTService = ioTService;
    }

    private PollDTO convert(Poll poll) {
        return modelMapper.map(poll, PollDTO.class);
    }

    private Poll convert(PollDTO pollDTO) {
        return modelMapper.map(pollDTO, Poll.class);
    }

    public List<PollDTO> getAllPolls() {
        return pollRepository.findAll().stream().map(this::convert).toList();
    }


    public PollDTO getPoll(Long id) {
        Poll poll = pollRepository.findById(id).get();
        if (poll.getStatus() == PollStatusEnum.NOT_INITIALISED) {
            if (poll.getStartTime() != null && poll.getStartTime().before(new Date())) {
                poll.setStatus(PollStatusEnum.ACTIVE);
                poll = pollRepository.save(poll);
            } else {
                throw new RuntimeException("Poll is not opend");
            }
        }
        if (poll.getStatus() == PollStatusEnum.ACTIVE
                && poll.getEndTime() != null
                && poll.getEndTime().before(new Date())) {
            poll.setStatus(PollStatusEnum.ENDED);
            poll = pollRepository.save(poll);
        }
        return convert(poll);
    }

    public PollDTO updatePollResult(Long id) {
        Poll poll = pollRepository.findById(id).get();
        for (Choice c : poll.getChoices()) {
            c.setVoteCount(voteRepository.sumOfVotesByChoiceIdAndPollInstId(c.getChoiceId(), id));
        }
        return convert(pollRepository.save(poll));
    }

    public PollDTO createPoll(PollDTO pollDTO) {
        Poll poll = convert(pollDTO);
        poll.setUser(getCurrentUser());
        poll.setStatus(PollStatusEnum.NOT_INITIALISED);
        poll = pollRepository.save(poll);
        poll.setRoomCode(generateRoomCode(poll.getPollId()));
        return convert(pollRepository.save(poll));
    }


    public PollDTO deletePoll(Long id) {
        Poll poll = pollRepository.findById(id).get();
        pollRepository.delete(poll);
        return convert(poll);
    }

    public List<PollDTO> getAllPollsByCurrentUser() {
        return pollRepository.findAllByUser(getCurrentUser()).stream().map(this::convert).toList();
    }

    public void vote(Long pollId, VoteDTO voteDTO) {
        Vote vote = new Vote();
        vote.setVoteCount(1);
        vote.setChoice(choiceRepository.findById(voteDTO.getChoiceId()).get());

        Poll poll = pollRepository.findById(pollId).get();

        if (poll.getStatus() != PollStatusEnum.ACTIVE) {
            throw new AccessDeniedException("Poll is not active. Has status " + poll.getStatus().toString());
        }
        if (poll.isNeedLogin()) {
            if (voteRepository.existsByVoterAndPoll(getCurrentUser(), poll)) {
                throw new AccessDeniedException("User has already voted");
            }
            vote.setVoter(getCurrentUser());
        }
        vote.setPoll(poll);
        if (voteDTO.getVoteCount() != null && voteDTO.getVoteCount() > 1) {
            User iot = getCurrentUser();
            if (iot.getUserType() != UserTypeEnum.IOT) {
                throw new RuntimeException("Only IoT devices can send more then one vote");
            }
            vote.setVoteCount(voteDTO.getVoteCount().intValue());
        }
        voteRepository.save(vote);
    }

    public PollDTO start(Long pollId) {
        Poll poll = pollRepository.findById(pollId).get();
        if (poll.getStatus() != PollStatusEnum.NOT_INITIALISED) {
            throw new AccessDeniedException("Poll can not be activated. Has status " + poll.getStatus().toString());
        }
        poll.setStartTime(new Date());
        poll.setStatus(PollStatusEnum.ACTIVE);
        return convert(pollRepository.save(poll));
    }

    public PollDTO end(Long pollId) {
        Poll poll = pollRepository.findById(pollId).get();
        if (poll.getStatus() != PollStatusEnum.ACTIVE) {
            throw new AccessDeniedException("Poll can not be ended. Has status " + poll.getStatus().toString());
        }
        poll.setEndTime(new Date());
        poll.setStatus(PollStatusEnum.ENDED);
        ioTService.removeIot(poll.getIotList());
        return convert(pollRepository.save(poll));
    }

    /**
     * Algorithm that generates unique room codes for
     * every id.
     * @param id poll Id
     * @return roomCode
     */
    private String generateRoomCode(Long id) {
        BigInteger largePrime = BigInteger.valueOf(1207571);
        long xorMask = 56876;
        long pow = 6;
        if (id*2 >= Math.pow(10, pow)) {
            while (Math.pow(10, pow) <= id*2) {
                pow += 1;
            }
        }
        long code = id;
        code = code ^ xorMask;
        code = BigInteger.valueOf(code).multiply(largePrime).mod(BigInteger.valueOf((long) Math.pow(10, pow))).longValue();
        return String.format("%0"+pow+"d", code);
    }
}
