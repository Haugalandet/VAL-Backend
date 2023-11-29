package no.haugalandplus.val.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.service.poll.PollService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PublisherService {
    private final String EXCHANGE_NAME = "subscriptionExchange";
    private final String QUEUE_NAME_RESULTS = "ResultsQueue";
    private final String QUEUE_NAME_META = "MetaQueue";
    private final String ROUTING_KEY_RESULTS = "results";

    private final RabbitTemplate rabbitTemplate;

    //hent resultat

    @Autowired
    public PublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }



    //erstatt melding med resultat
    public void publishMessage(String resultJSON) {

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_RESULTS, resultJSON);
    }
}
