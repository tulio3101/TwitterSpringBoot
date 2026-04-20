package edu.tdse.events.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import edu.tdse.events.PostDeletedEvent;
import edu.tdse.services.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostDeletedEventListener {

    private final StreamService streamService;

    @RabbitListener(queues = "stream.post.deleted.queue")
    public void handlePostDeleted(PostDeletedEvent event) {
        log.info("Received post.deleted event for postId={}", event.getPostId());
        streamService.removePostFromStream(event.getPostId());
    }

}
