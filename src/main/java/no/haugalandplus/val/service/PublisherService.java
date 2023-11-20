package no.haugalandplus.val.service;

package harecon.harecon.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PublisherService {
    private final String EXCHANGE_NAME = "subscriptionExchange";
    private final String QUEUE_NAME_RESULTS = "ResultsQueue";
    private final String QUEUE_NAME_META = "MetaQueue";
    private final String ROUTING_KEY_RESULTS = "results";
    private final String ROUTING_KEY_META = "resultsformeta";

    private final RabbitTemplate rabbitTemplate;

    //hent resultat

    @Autowired
    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //erstatt melding med resultat
    public void publishMessage(String message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_RESULTS, message);
    }
}
