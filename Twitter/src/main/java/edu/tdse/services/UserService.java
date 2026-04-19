package edu.tdse.services;

import edu.tdse.exception.UserNotFoundException;

import org.springframework.stereotype.Service;

import edu.tdse.mapper.UserMapper;
import edu.tdse.models.dto.response.UserResponseDTO;
import edu.tdse.models.entity.User;
import edu.tdse.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional
    public UserResponseDTO getPersonalInfo(String id){

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuario No Encontrado"));

        UserResponseDTO response = userMapper.toDto(user);

        return response;
 
    }



}
