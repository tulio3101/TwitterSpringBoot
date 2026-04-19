package edu.tdse.mapper;

import org.mapstruct.Mapper;

import edu.tdse.models.entity.Post;
import edu.tdse.models.dto.request.PostRequestDTO;
import edu.tdse.models.dto.response.PostResponseDTO;

@Mapper(componentModel = "spring")
public interface PostMapper {
   
    Post toEntity(PostRequestDTO dto);

    PostResponseDTO toDto (Post entity);

}