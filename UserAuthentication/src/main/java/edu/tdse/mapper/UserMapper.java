package edu.tdse.mapper;

import org.mapstruct.Mapper;

import edu.tdse.models.dto.request.UserRequestDTO;
import edu.tdse.models.dto.response.UserResponseDTO;
import edu.tdse.models.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
   
    User toEntity(UserRequestDTO dto);

    UserResponseDTO toDto(User entity);

}
