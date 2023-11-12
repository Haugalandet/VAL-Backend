package no.haugalandplus.val.service;

import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PollServiceMock extends PollService {

    public PollServiceMock(PollRepository pollRepository, ModelMapper modelMapper, VoteRepository voteRepository, ChoiceRepository choiceRepository, IoTService ioTService) {
        super(pollRepository, modelMapper, voteRepository, choiceRepository, ioTService);
    }

    private Date currentDate;

    @Override
    protected Date clock() {
        if (currentDate != null) {
            return currentDate;
        } else {
            return super.clock();
        }
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
