package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.PollChoiceDTO;
import no.haugalandplus.val.entities.PollChoice;
import no.haugalandplus.val.repository.PollChoiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PollChoiceService {

    private PollChoiceRepository pollChoiceRepository;
    private ModelMapper modelMapper;

    public PollChoiceService(PollChoiceRepository pollChoiceRepository, ModelMapper modelMapper) {
        this.pollChoiceRepository = pollChoiceRepository;
        this.modelMapper = modelMapper;
    }

    public PollChoiceDTO get(Long id) {
        PollChoice pollChoice = pollChoiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID does not exist!"));
        return convertToDTO(pollChoice);
    }

    public List<PollChoiceDTO> getAll() {
        return pollChoiceRepository.findAll()
                .stream().map(this::convertToDTO).toList();
    }

    public PollChoiceDTO saveOrUpdate(PollChoiceDTO pollChoiceDTO) {
        return convertToDTO(pollChoiceRepository.save(convertFromDTO(pollChoiceDTO)));
    }

    public PollChoiceDTO delete(Long id) {
        PollChoice pollChoice = pollChoiceRepository.findById(id).get();
        pollChoiceRepository.delete(pollChoice);
        return convertToDTO(pollChoice);
    }

    public PollChoiceDTO convertToDTO(PollChoice pollChoice) {
        return modelMapper.map(pollChoice, PollChoiceDTO.class);
    }

    public PollChoice convertFromDTO(PollChoiceDTO pollChoiceDTO) {
        return modelMapper.map(pollChoiceDTO, PollChoice.class);
    }


}
