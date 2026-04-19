
package edu.tdse.services;

import org.springframework.stereotype.Service;

import edu.tdse.exception.PostNotFoundException;
import edu.tdse.mapper.PostMapper;
import edu.tdse.models.dto.request.PostRequestDTO;
import edu.tdse.models.dto.response.PostResponseDTO;
import edu.tdse.models.entity.Post;
import edu.tdse.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService{

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final StreamService streamService;


    @Transactional
    public PostResponseDTO createPost(PostRequestDTO dto){

        Post post = postMapper.toEntity(dto);

        Post postToSave = postRepository.save(post);

        streamService.addPostToStream(postToSave.getPostId());

        return postMapper.toDto(postToSave);

    }


    @Transactional
    public PostResponseDTO updatePost(PostRequestDTO dto){

        Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new PostNotFoundException("Post Not Found to Update"));

        post.setMessage(dto.getMessage());
        
        Post postUpdated = postRepository.save(post);

        
        return postMapper.toDto(postUpdated);
    }



    @Transactional
    public void deletePost(String postId){

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with: {postId} not found"));

        postRepository.delete(post);

    }


    @Transactional
    public PostResponseDTO getPostById(String postId){

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with : {postId} not found"));

        return postMapper.toDto(post);

    }


}