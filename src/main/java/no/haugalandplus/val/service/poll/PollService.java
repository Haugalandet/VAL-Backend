package no.haugalandplus.val.service.poll;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
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
import no.haugalandplus.val.service.IoTService;
import no.haugalandplus.val.service.PublisherService;
import no.haugalandplus.val.service.ServiceUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Date;
import java.util.List;

@Service
public class PollService extends ServiceUtils {

    private final PollRepository pollRepository;
    private final ModelMapper modelMapper;
    private final VoteRepository voteRepository;
    private final ChoiceRepository choiceRepository;
    private final IoTService ioTService;
    private final PublisherService publisherService;

    @PersistenceContext
    private EntityManager entityManager;

    public PollService(PollRepository pollRepository, ModelMapper modelMapper, VoteRepository voteRepository, ChoiceRepository choiceRepository, IoTService ioTService, PublisherService publisherService, EntityManager entityManager) {
        this.pollRepository = pollRepository;
        this.modelMapper = modelMapper;
        this.voteRepository = voteRepository;
        this.choiceRepository = choiceRepository;
        this.ioTService = ioTService;
        this.publisherService = publisherService;
        this.entityManager = entityManager;
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
        return pollRepository.findAll().stream().map(this::convert).toList();
    }


    public PollDTO getPoll(Long id) {
        Poll poll = pollRepository.findById(id).get();
        return convert(poll);
    }

    public PollDTO getPollWithRoomCode(String roomCode) {
        Poll poll = pollRepository.findByRoomCode(roomCode);
        return convert(poll);
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

    @Transactional
    public void deletePoll(Long id) {
        voteRepository.deleteAllByPollId(id);
        pollRepository.deleteAllByPollId(id);
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
        if (poll.getStatus() == PollStatusEnum.ACTIVE) {
            throw new AccessDeniedException("Poll can not be activated.");
        }

        poll = setPollEndTime(poll, startPollDTO.getEndTime());
        poll = setPollStartTime(poll, startPollDTO.getStartTime());

        return convert(pollRepository.save(poll));
    }

    private Poll setPollEndTime(Poll poll, Date endTime) {
        poll.setEndTime(endTime);
        return poll;
    }

    private Poll setPollStartTime(Poll poll, Date startTime) {
        if (startTime != null) {
            poll.setStartTime(startTime);
        } else {
            poll = startPoll(poll);
        }
        return poll;
    }

    public PollDTO end(Long pollId) {
        Poll poll = pollRepository.findById(pollId).get();
        if (poll.getStatus() != PollStatusEnum.ACTIVE) {
            throw new AccessDeniedException("Poll can not be ended. Has status " + poll.getStatus().toString());
        }
        return convert(endPoll(poll));
    }

    public Date clock() {
        return new Date();
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deletePollsByUserId(Long userId) {
        voteRepository.deleteAllByUserId(userId);
        choiceRepository.deleteAllByUserId(userId);
        pollRepository.deleteAllByUserId(userId);
        voteRepository.anonymizeAllVotesWithUserId(userId);
    }


    @Transactional
    public Poll startPoll(Poll poll) {
        Poll lockedPoll = entityManager.find(Poll.class, poll.getPollId(), LockModeType.PESSIMISTIC_WRITE);

        Date startTime = lockedPoll.getStartTime();
        if (startTime == null) {
            startTime = clock();
        }
        lockedPoll.setStartTime(startTime);
        lockedPoll.setStatus(PollStatusEnum.ACTIVE);
        lockedPoll.getChoices().forEach(c -> {
            c.setVoteCount(0);
            entityManager.merge(c);
        });
        voteRepository.deleteAllByPoll(lockedPoll);

        return entityManager.merge(lockedPoll);
    }

    @Transactional
    public Poll endPoll(Poll poll) {
        Poll lockedPoll = entityManager.find(Poll.class, poll.getPollId(), LockModeType.PESSIMISTIC_WRITE);

        Date endTime = lockedPoll.getEndTime();
        if (endTime == null) {
            endTime = clock();
        }
        lockedPoll.setEndTime(endTime);
        lockedPoll.setStatus(PollStatusEnum.ENDED);
        ioTService.removeIot(lockedPoll);

        try {
            publisherService.publishMessage(new ObjectMapper().writeValueAsString(convert(lockedPoll)));
        } catch (Exception e) {
            // Handle the exception appropriately
        }

        return entityManager.merge(lockedPoll);
    }
}
