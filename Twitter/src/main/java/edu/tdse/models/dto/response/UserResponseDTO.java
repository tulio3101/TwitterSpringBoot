package edu.tdse.models.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UserResponseDTO{

    private String name;
    private String email;
    private String password;
    private List<String> postsId;

}