package edu.tdse.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import edu.tdse.models.dto.request.PostRequestDTO;
import edu.tdse.models.dto.response.PostResponseDTO;
import edu.tdse.models.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
   
    Post toEntity(PostRequestDTO dto);

    PostResponseDTO toDto(Post entity);

    List<PostResponseDTO> toDto(List<Post> entities);

}
