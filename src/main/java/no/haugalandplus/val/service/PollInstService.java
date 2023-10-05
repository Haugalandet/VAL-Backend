package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.PollChoiceDTO;
import no.haugalandplus.val.dto.PollInstDTO;
import no.haugalandplus.val.dto.PollResultDTO;
import no.haugalandplus.val.entities.PollInst;
import no.haugalandplus.val.entities.PollResult;
import no.haugalandplus.val.repository.PollInstRepository;
import no.haugalandplus.val.repository.PollResultRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollInstService {

    private PollInstRepository pollInstRepository;
    private PollResultRepository pollResultRepository;
    private ModelMapper modelMapper;

    public PollInstService(PollInstRepository pollInstRepository, PollResultRepository pollResultRepository, ModelMapper modelMapper) {
        this.pollInstRepository = pollInstRepository;
        this.pollResultRepository = pollResultRepository;
        this.modelMapper = modelMapper;
    }

    public PollInstDTO convert(PollInst pollInst) {
        PollInstDTO pollInstDTO = modelMapper.map(pollInst, PollInstDTO.class);
        pollInstDTO.setPollId(pollInst.getPoll().getPollId());

        pollInstDTO.setPollResult(pollResultRepository.findPollResultsByPollInst(pollInst).stream()
                .map(r -> {
                    PollResultDTO pollResultDTO = modelMapper.map(r, PollResultDTO.class);
                    pollResultDTO.setPollChoice(modelMapper.map(r.getPollChoice(), PollChoiceDTO.class));
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

    public PollInstDTO save(PollInstDTO pollInstDTO) {
        return convert(pollInstRepository.save(convert(pollInstDTO)));
    }

    public PollInstDTO deletePollInst(Long id) {
        PollInst pollInst = pollInstRepository.findById(id).get();
        pollInstRepository.delete(pollInst);
        return convert(pollInst);
    }
}
