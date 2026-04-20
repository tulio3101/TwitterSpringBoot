package edu.tdse.eventBus;

public interface EventPublisher {

    void publish(Object event, String routingKey);

}
