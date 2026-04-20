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
    public Queue postCreatedQueue(){
        return new Queue("post.created.queue", true);
    }

    @Bean
    public Binding bindingPostCreated(){
        return BindingBuilder.bind(postCreatedQueue()).to(postExchange()).with("post.created");
    }

    @Bean
    public TopicExchange postExchange(){
        return new TopicExchange("post.exchange",true,false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
