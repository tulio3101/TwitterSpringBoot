package edu.tdse.mapper;

import org.mapstruct.Mapper;

import edu.tdse.models.dto.request.StreamRequestDTO;
import edu.tdse.models.dto.response.StreamResponseDTO;
import edu.tdse.models.entity.Stream;

@Mapper(componentModel = "spring")
public interface StreamMapper {
   
   Stream toEntity(StreamRequestDTO dto);
   
   StreamResponseDTO toDto(Stream entity);

}
