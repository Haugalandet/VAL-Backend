package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.ChoiceDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChoiceService {

    private ChoiceRepository choiceRepository;
    private PollRepository pollRepository;
    private ModelMapper modelMapper;

    public ChoiceService(ChoiceRepository choiceRepository, PollRepository pollRepository, ModelMapper modelMapper) {
        this.choiceRepository = choiceRepository;
        this.pollRepository = pollRepository;
        this.modelMapper = modelMapper;
    }

    public ChoiceDTO get(Long id) {
        Choice choice = choiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID does not exist!"));
        return convertToDTO(choice);
    }

    public List<ChoiceDTO> getAll() {
        return choiceRepository.findAll()
                .stream().map(this::convertToDTO).toList();
    }

    public ChoiceDTO saveOrUpdate(Long pollId, ChoiceDTO choiceDTO) {
        Choice choice = convertFromDTO(choiceDTO);
        choice.setPoll(pollRepository.findById(pollId).get());
        return convertToDTO(choiceRepository.save(choice));
    }

    public ChoiceDTO delete(Long id) {
        Choice choice = choiceRepository.findById(id).get();
        choiceRepository.delete(choice);
        return convertToDTO(choice);
    }

    public ChoiceDTO convertToDTO(Choice choice) {
        return modelMapper.map(choice, ChoiceDTO.class);
    }

    public Choice convertFromDTO(ChoiceDTO choiceDTO) {
        return modelMapper.map(choiceDTO, Choice.class);
    }


}
