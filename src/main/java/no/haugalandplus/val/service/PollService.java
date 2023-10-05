package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.repository.ConfigRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollService {

    private PollRepository pollRepository;
    private ModelMapper modelMapper;

    public PollService(PollRepository pollRepository, ModelMapper modelMapper) {
        this.pollRepository = pollRepository;
        this.modelMapper = modelMapper;
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
        return convert(pollRepository.findById(id).get());
    }

    public PollDTO savePoll(PollDTO poll) {
        return convert(pollRepository.save(convert(poll)));
    }

    public PollDTO deletePoll(Long id) {
        Poll poll = pollRepository.findById(id).get();
        pollRepository.delete(poll);
        return convert(poll);
    }
}
