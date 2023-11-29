package no.haugalandplus.val.mock;

import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.VoteRepository;
import no.haugalandplus.val.service.IoTService;
import no.haugalandplus.val.service.PublisherService;
import no.haugalandplus.val.service.poll.PollService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

@Primary
@Service
public class PollServiceMock extends PollService {

    @Autowired
    private ClockMock clockMock;

    public PollServiceMock(PollRepository pollRepository, ModelMapper modelMapper, VoteRepository voteRepository, ChoiceRepository choiceRepository, IoTService ioTService, PublisherService publisherService) {
        super(pollRepository, modelMapper, voteRepository, choiceRepository, ioTService, publisherService);
    }


    @Override
    public Date clock() {
        if (clockMock.clock() != null) {
            return clockMock.clock();
        } else {
            return super.clock();
        }
    }
}
