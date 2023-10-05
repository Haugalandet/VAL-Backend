package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.PollInstDTO;
import no.haugalandplus.val.entities.PollInst;
import no.haugalandplus.val.repository.PollInstRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollInstService {

    private PollInstRepository pollInstRepository;
    private ModelMapper modelMapper;

    public PollInstService(PollInstRepository pollInstRepository, ModelMapper modelMapper) {
        this.pollInstRepository = pollInstRepository;
        this.modelMapper = modelMapper;
    }

    public PollInstDTO convert(PollInst pollInst) {
        return modelMapper.map(pollInst, PollInstDTO.class);
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
