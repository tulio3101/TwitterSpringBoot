
package edu.tdse.services;

import java.util.List;

import org.springframework.stereotype.Service;

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
    private final StreamService streamService;
    private final UserService userService;


    public PostResponseDTO createPost(PostRequestDTO dto){

        Post post = postMapper.toEntity(dto);

        Post postToSave = postRepository.save(post);

        streamService.addPostToStream(postToSave.getPostId());

        userService.addPostToUser(postToSave.getUserId(), postToSave.getPostId());

        return postMapper.toDto(postToSave);

    }


    public PostResponseDTO updatePost(String postId, PostRequestDTO dto){

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post Not Found to Update"));

        post.setMessage(dto.getMessage());

        return postMapper.toDto(postRepository.save(post));
    }



    public void deletePost(String postId){

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with: " + postId + " not found"));

        streamService.removePostFromStream(postId);
        postRepository.delete(post);

    }


    public List<PostResponseDTO> getAllPosts(){

        List<Post> posts = postRepository.findAll();

        return postMapper.toDto(posts);

    }


}
