package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.ChoiceDTO;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.repository.PollRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PollService extends ServiceUtils {

    private PollRepository pollRepository;
    private ModelMapper modelMapper;

    public PollService(PollRepository pollRepository, ModelMapper modelMapper) {
        this.pollRepository = pollRepository;
        this.modelMapper = modelMapper;
    }

    private PollDTO convert(Poll poll) {
        /*
        PollDTO pollDTO = new PollDTO();
        pollDTO.setPollId(poll.getPollId());
        pollDTO.setAnon(poll.isAnon());
        pollDTO.setName(poll.getName());
        pollDTO.setDescription(poll.getDescription());

        List<ChoiceDTO> choiceDTOs = new ArrayList<>();

        for (Choice c : poll.getChoices()) {
            ChoiceDTO choiceDTO = new ChoiceDTO();
            choiceDTO.setDescription(c.getDescription());
            choiceDTO.setName(c.getName());
            choiceDTO.setChoiceId(c.getChoiceId());
            choiceDTOs.add(choiceDTO);
        }

        pollDTO.setChoices(choiceDTOs);
        return pollDTO;

         */
        return modelMapper.map(poll, PollDTO.class);
    }

    private Poll convert(PollDTO pollDTO) {
        /*
        Poll poll = new Poll();
        poll.setDescription(pollDTO.getDescription());
        poll.setName(pollDTO.getName());
        poll.setAnon(pollDTO.isAnon());
        //poll.setUser(getCurrentUser());
        poll.setPollId(pollDTO.getPollId());

        List<Choice> choices = new ArrayList<>();

        for (ChoiceDTO c : pollDTO.getChoices()) {
            Choice choice = new Choice();
            choice.setDescription(c.getDescription());
            choice.setName(c.getName());
            choice.setChoiceId(c.getChoiceId());
            choice.setPoll(poll);
            choices.add(choice);
        }

        poll.setChoices(choices);
        return poll;

         */
        return modelMapper.map(pollDTO, Poll.class);
    }

    public List<PollDTO> getAllPolls() {
        return pollRepository.findAll().stream().map(this::convert).toList();
    }


    public PollDTO getPoll(Long id) {
        return convert(pollRepository.findById(id).get());
    }

    public PollDTO createPoll(PollDTO pollDTO) {
        Poll poll = convert(pollDTO);
        poll.setUser(getCurrentUser());
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
}
