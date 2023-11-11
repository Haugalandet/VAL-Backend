package no.haugalandplus.val.service;

import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.dto.StartPollDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.Vote;
import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.hibernate.sql.ast.tree.expression.Star;
import org.modelmapper.ModelMapper;
import org.springframework.core.SpringVersion;
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
        PollDTO pollDTO = modelMapper.map(poll, PollDTO.class);
        if (getCurrentUserSafe() != null) {
            pollDTO.setHasUserVoted(voteRepository.existsByVoterAndPoll(getCurrentUserSafe(), poll));
        }
        return pollDTO;
    }

    private Poll convert(PollDTO pollDTO) {
        Poll poll = modelMapper.map(pollDTO, Poll.class);
        poll.getChoices().forEach(c -> c.setPoll(poll));
        return poll;
    }

    public List<PollDTO> getAllPolls() {
        return pollRepository.findAll().stream().map(p->convert(processPoll(p))).toList();
    }


    public PollDTO getPoll(Long id) {
        Poll poll = pollRepository.findById(id).get();
        return convert(processPoll(poll));
    }

    public PollDTO getPollWithRoomCode(String roomCode) {
        Poll poll = pollRepository.findByRoomCode(roomCode);
        return convert(processPoll(poll));
    }

    private Poll processPoll(Poll poll) {
        if (poll.getStatus() != PollStatusEnum.ACTIVE
                && poll.getStartTime() != null
                && poll.getStartTime().before(clock())) {
            startPoll(poll, poll.getStartTime());
            poll = pollRepository.save(poll);
        }
        if (poll.getStatus() == PollStatusEnum.ACTIVE
                && poll.getEndTime() != null
                && poll.getEndTime().before(clock())) {
            poll.setStatus(PollStatusEnum.ENDED);
            poll = pollRepository.save(poll);
        }
        return poll;
    }

    public PollDTO updatePollResult(Long id) {
        Poll poll = pollRepository.findById(id).get();
        for (Choice c : poll.getChoices()) {
            c.setVoteCount(voteRepository.sumOfVotesByChoiceId(c.getChoiceId()));
        }
        return convert(pollRepository.save(poll));
    }

    public PollDTO createPoll(PollDTO pollDTO) {
        Poll poll = convert(pollDTO);
        poll.setUser(getCurrentUser());
        poll.setStatus(PollStatusEnum.NOT_INITIALISED);
        poll = pollRepository.save(poll);
        poll.setRoomCode(RoomCodeHelper.generateRoomCode(poll.getPollId()));
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
        vote.setVoter(getCurrentUserSafe());
        voteRepository.save(vote);
    }

    public PollDTO start(Long pollId, StartPollDTO startPollDTO) {
        Poll poll = pollRepository.findById(pollId).get();
        poll = processPoll(poll);
        if (poll.getStatus() == PollStatusEnum.ACTIVE) {
            throw new AccessDeniedException("Poll can not be activated.");
        }
        if (startPollDTO != null) {
            if (startPollDTO.getStartTime() != null) {
                if (startPollDTO.getStartTime().before(clock())) {
                    throw new RuntimeException("Time already past");
                }
                poll.setStartTime(startPollDTO.getStartTime());
            } else {
                startPoll(poll);
            }

            if (startPollDTO.getEndTime() != null && startPollDTO.getEndTime().before(clock())) {
                throw new RuntimeException("Time already past");
            }
            poll.setEndTime(startPollDTO.getEndTime());
        } else {
            startPoll(poll);
        }

        return convert(pollRepository.save(poll));
    }

    private void startPoll(Poll poll) {
        startPoll(poll, clock());
    }

    private void startPoll(Poll poll, Date startTime) {
        poll.setStartTime(startTime);
        poll.setStatus(PollStatusEnum.ACTIVE);
        poll.getChoices().forEach( c -> {
            c.setVoteCount(0);
            choiceRepository.save(c);
            voteRepository.deleteAllByChoice(c);
        });
    }

    public PollDTO end(Long pollId) {
        Poll poll = pollRepository.findById(pollId).get();
        poll = processPoll(poll);
        if (poll.getStatus() != PollStatusEnum.ACTIVE) {
            throw new AccessDeniedException("Poll can not be ended. Has status " + poll.getStatus().toString());
        }
        poll.setEndTime(clock());
        poll.setStatus(PollStatusEnum.ENDED);
        ioTService.removeIot(poll);
        return convert(pollRepository.save(poll));
    }

    protected Date clock() {
        return new Date();
    }
}
