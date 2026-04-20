package edu.tdse.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.tdse.models.entity.Stream;
import edu.tdse.repository.StreamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StreamService {
    
    private final StreamRepository streamRepository;


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

    public Stream addPostToStream(String postId){

        Stream stream = getStream();

        if (!stream.getPostsId().contains(postId)){
            stream.getPostsId().add(0, postId);
            streamRepository.save(stream);
        }

        return stream;

    }


    public Stream removePostFromStream(String postId){
        Stream stream = getStream();
        stream.getPostsId().remove(postId);
        return streamRepository.save(stream);
    }

}
