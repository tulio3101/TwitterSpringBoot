package edu.tdse.events.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import edu.tdse.events.PostCreatedEvent;
import edu.tdse.services.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostCreatedEventListener {

    private final StreamService streamService;

    @RabbitListener(queues = "stream.post.created.queue")
    public void handlePostCreated(PostCreatedEvent event) {
        log.info("Received post.created event for postId={}", event.getPostId());
        streamService.addPostToStream(event.getPostId());
    }

}
