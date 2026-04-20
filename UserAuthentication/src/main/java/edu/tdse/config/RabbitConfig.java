package edu.tdse.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue userCreatedQueue(){
        return new Queue("user.created.queue", true);
    }

    @Bean
    public TopicExchange userExchange(){
        return new TopicExchange("user.exchange", true, false);
    }

    @Bean
    public Binding bindingUserCreated(){
        return BindingBuilder.bind(userCreatedQueue()).to(userExchange()).with("user.created");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
