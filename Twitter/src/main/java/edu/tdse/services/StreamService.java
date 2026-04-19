package edu.tdse.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import edu.tdse.models.entity.Stream;
import edu.tdse.exception.StreamNotFoundException;
import edu.tdse.repository.PostRepository;
import edu.tdse.repository.StreamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StreamService {
   
    private final StreamRepository streamRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    

    @Transactional
    public Stream getStream(){

        List<Stream> streams = streamRepository.findAll();

        if (streams.isEmpty()){
            Stream newStream = Stream.builder()
                .streamId("main-stream")
                .postsId(new ArrayList<>())
                .build();
            
            return streamRepository.save(newStream);
        }

        return streams.get(0);

    }

    @Transactional
    public Stream addPostToStream(String postId){

        postRepository.findById(postId).orElseThrow(() -> new StreamNotFoundException("Post not found"));

        Stream stream = getStream();

        if (!stream.getPostsId().contains(postId)){
            stream.getPostsId().add(postId);
            streamRepository.save(stream);
        }

        return stream;

    }


    @Transactional
    public Stream removePostFromStream(String postId){
        Stream stream = getStream();
        stream.getPostsId().remove(postId);
        postService.deletePost(postId);
        return streamRepository.save(stream);
    }

    public List<String> getAllPostsIdFromStream(){
        return getStream().getPostsId();
    }

    @Transactional
    public Stream getStreamById(String streamId){

        Stream stream = streamRepository.findById(streamId).orElseThrow(() -> new StreamNotFoundException("Stream not found"));
        
        return stream;

    }

}
