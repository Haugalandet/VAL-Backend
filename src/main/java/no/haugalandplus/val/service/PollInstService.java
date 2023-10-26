package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.ChoiceDTO;
import no.haugalandplus.val.dto.PollInstDTO;
import no.haugalandplus.val.dto.PollResultDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.PollInst;
import no.haugalandplus.val.entities.PollResult;
import no.haugalandplus.val.entities.Vote;
import no.haugalandplus.val.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PollInstService extends ServiceUtils {

    private PollInstRepository pollInstRepository;
    private PollResultRepository pollResultRepository;
    private VoteRepository voteRepository;
    private PollRepository pollRepository;
    private ChoiceRepository choiceRepository;
    private ModelMapper modelMapper;

    public PollInstService(PollInstRepository pollInstRepository, PollResultRepository pollResultRepository, VoteRepository voteRepository, PollRepository pollRepository, ChoiceRepository choiceRepository, ModelMapper modelMapper) {
        this.pollInstRepository = pollInstRepository;
        this.pollResultRepository = pollResultRepository;
        this.voteRepository = voteRepository;
        this.pollRepository = pollRepository;
        this.choiceRepository = choiceRepository;
        this.modelMapper = modelMapper;
    }

    public PollInstDTO convert(PollInst pollInst) {
        PollInstDTO pollInstDTO = modelMapper.map(pollInst, PollInstDTO.class);
        pollInstDTO.setPollId(pollInst.getPoll().getPollId());

        pollInstDTO.setPollResult(pollResultRepository.findPollResultsByPollInst(pollInst).stream()
                .map(r -> {
                    PollResultDTO pollResultDTO = modelMapper.map(r, PollResultDTO.class);
                    pollResultDTO.setPollChoice(modelMapper.map(r.getChoice(), ChoiceDTO.class));
                    pollResultDTO.setTotalCount(voteRepository.sumOfVotesByChoiceIdAndPollInstId(r.getChoice().getChoiceId(), pollInst));
                    return pollResultDTO;
                }).toList());

        return pollInstDTO;
    }

    public PollInst convert(PollInstDTO pollInstDTO) {
        return modelMapper.map(pollInstDTO, PollInst.class);
    }

    public List<PollInstDTO> getAllPollInsts(Long pollId) {
        return pollInstRepository.findAllByPollId(pollId).stream().map(this::convert).toList();
    }

    public PollInstDTO getPollInst(Long id) {
        return convert(pollInstRepository.findById(id).get());
    }

    public PollInstDTO insertPollInst(Long pollId, PollInstDTO pollInstDTO) {
        PollInst pollInst = convert(pollInstDTO);
        pollInst.setPoll(pollRepository.findById(pollId).get());
        pollInst = pollInstRepository.save(pollInst);
        pollInst.setRoomCode(generateRoomCode(pollInst.getPollInstId()));
        PollInst savedpollInst = pollInstRepository.save(pollInst);

        List<PollResult> pollResults = choiceRepository.findAllByPoll(pollInst.getPoll()).stream()
                .map(choice -> {
                    PollResult pollResult = new PollResult();
                    pollResult.setChoice(choice);
                    pollResult.setPollInst(savedpollInst);
                    return pollResult;
                }).toList();
        pollResultRepository.saveAll(pollResults);
        return convert(savedpollInst);
    }

    public PollInstDTO save(Long pollId, PollInstDTO pollInstDTO) {
        PollInst pollInst = convert(pollInstDTO);
        pollInst.setPoll(pollRepository.findById(pollId).get());
        return convert(pollInstRepository.save(pollInst));
    }

    public PollInstDTO deletePollInst(Long id) {
        PollInst pollInst = pollInstRepository.findById(id).get();
        pollInstRepository.delete(pollInst);
        return convert(pollInst);
    }

    public boolean vote(Long pollInstId, VoteDTO voteDTO) {
        Vote vote = modelMapper.map(voteDTO, Vote.class);
        vote.setVoteId(0);
        vote.setVoter(getCurrentUser());
        vote.setPollInst(pollInstRepository.findById(pollInstId).get());
        if (vote.getVoteCount() == null || vote.getVoteCount() == 0) {
            vote.setVoteCount(1);
        }
        voteRepository.save(vote);
        return true;
    }

    public PollInstDTO start(Long pollInstId) {
        PollInst pollInst = pollInstRepository.findById(pollInstId).get();
        pollInst.setStartTime(new Date());
        return convert(pollInstRepository.save(pollInst));
    }

    public PollInstDTO end(Long pollInstId) {
        PollInst pollInst = pollInstRepository.findById(pollInstId).get();
        pollInst.setEndTime(new Date());
        return convert(pollInstRepository.save(pollInst));
    }

    /**
     * Algorithm that generates unique room codes for
     * every id.
     * @param id
     * @return roomCode
     */
    private String generateRoomCode(Long id) {
        long largePrime = 1207571;
        long xorMask = 98765;
        long pow = 6;
        if (id >= Math.pow(10, pow)) {
            while (Math.pow(10, pow) <= id) {
                pow += 1;
            }
        }
        long code = id;
        code = code ^ xorMask;
        code = ((code * largePrime) % (long) Math.pow(10, pow));
        return String.format("%0"+pow+"d", code);
    }
}
