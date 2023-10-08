package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.ChoiceDTO;
import no.haugalandplus.val.dto.PollInstDTO;
import no.haugalandplus.val.dto.PollResultDTO;
import no.haugalandplus.val.dto.VoteDTO;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.PollInst;
import no.haugalandplus.val.entities.PollResult;
import no.haugalandplus.val.entities.Vote;
import no.haugalandplus.val.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollInstService extends Utils {

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
                    pollResultDTO.setTotalCount(voteRepository.findAllByChoiceAndPollInst(r.getChoice(), pollInst)
                            .stream()
                            .reduce(0,(a, b) -> a + b.getVoteCount(), Integer::sum)
                    );
                    return pollResultDTO;
                }).toList());

        return pollInstDTO;
    }

    public PollInst convert(PollInstDTO pollInstDTO) {
        return modelMapper.map(pollInstDTO, PollInst.class);
    }

    public List<PollInstDTO> getAllPollInsts() {
        return pollInstRepository.findAll().stream().map(this::convert).toList();
    }

    public PollInstDTO getPollInst(Long id) {
        return convert(pollInstRepository.findById(id).get());
    }

    public PollInstDTO insertPollInst(Long pollId, PollInstDTO pollInstDTO) {
        PollInst pollInst = convert(pollInstDTO);
        pollInst.setPoll(pollRepository.findById(pollId).get());
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
        return true;
    }
}
