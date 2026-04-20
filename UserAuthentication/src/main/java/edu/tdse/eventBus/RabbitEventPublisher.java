package edu.tdse.eventBus;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RabbitEventPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(Object event, String routingKey) {
        rabbitTemplate.convertAndSend("user.exchange", routingKey, event);
    }

}
