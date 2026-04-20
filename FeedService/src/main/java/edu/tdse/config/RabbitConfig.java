package edu.tdse.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    

    @Bean
    public Queue streamPostCreated(){
        return new Queue("stream.post.created.queue", true);
    }

    @Bean
    public Queue streamPostDeleted(){
        return new Queue("stream.post.deleted.queue", true);
    }

    @Bean
    public TopicExchange postExchange(){
        return new TopicExchange("post.exchange", true, false);
    }

    @Bean
    public Binding bindingStreamPostCreated(){
        return BindingBuilder
                .bind(streamPostCreated())
                .to(postExchange())
                .with("post.created");
    }

    @Bean
    public Binding bindingStreamPostDeleted(){
        return BindingBuilder
                .bind(streamPostDeleted())
                .to(postExchange())
                .with("post.deleted");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
