package edu.tdse.services;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.tdse.eventBus.EventPublisher;
import edu.tdse.events.PostCreatedEvent;
import edu.tdse.events.PostDeletedEvent;
import edu.tdse.events.PostUpdatedEvent;
import edu.tdse.exception.PostNotFoundException;
import edu.tdse.mapper.PostMapper;
import edu.tdse.models.dto.request.PostRequestDTO;
import edu.tdse.models.dto.response.PostResponseDTO;
import edu.tdse.models.entity.Post;
import edu.tdse.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService{

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final EventPublisher eventPublisher;

    public PostResponseDTO createPost(PostRequestDTO dto){

        Post post = postMapper.toEntity(dto);

        Post postToSave = postRepository.save(post);

        PostCreatedEvent postEvent = PostCreatedEvent.builder()
                        .postId(postToSave.getPostId())
                        .build();

        eventPublisher.publish(postEvent, "post.created");

        return postMapper.toDto(postToSave);

    }


    public PostResponseDTO updatePost(String postId, PostRequestDTO dto){

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post Not Found to Update"));

        post.setMessage(dto.getMessage());

        Post postUpdated = postRepository.save(post);

        PostUpdatedEvent postEvent = PostUpdatedEvent.builder()
                            .postId(postUpdated.getPostId())
                            .build();

        eventPublisher.publish(postEvent, "post.updated");

        return postMapper.toDto(postUpdated);
    }



    public void deletePost(String postId){

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with: {postId} not found"));

        PostDeletedEvent postEvent = PostDeletedEvent.builder()
                        .postId(post.getPostId())
                        .build();

        eventPublisher.publish(postEvent, "post.deleted");

        postRepository.delete(post);

    }


    public List<PostResponseDTO> getAllPosts(){

        List<Post> posts = postRepository.findAll();

        return postMapper.toDto(posts);

    }


}
