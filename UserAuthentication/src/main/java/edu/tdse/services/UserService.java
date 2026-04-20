package edu.tdse.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import edu.tdse.eventBus.EventPublisher;
import edu.tdse.events.UserCreatedEvent;
import edu.tdse.exception.UserNotFoundException;
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
    private final EventPublisher eventPublisher;

    @Transactional
    public UserResponseDTO getPersonalInfo(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserResponseDTO registerUser(String id, String name, String email) {
        return userRepository.findById(id)
            .map(userMapper::toDto)
            .orElseGet(() -> {
                User newUser = User.builder()
                    .id(id)
                    .name(name)
                    .email(email)
                    .postsId(new ArrayList<>())
                    .build();
                UserResponseDTO saved = userMapper.toDto(userRepository.save(newUser));

                UserCreatedEvent userEvent = UserCreatedEvent.builder()
                    .userId(id)
                    .name(name)
                    .email(email)
                    .build();
                eventPublisher.publish(userEvent,"user.created");
                return saved;
            });
    }

    @Transactional
    public void addPostToUser(String userId, String postId){

      User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));

      List<String> postsUser = user.getPostsId();

      postsUser.add(postId);

      user.setPostsId(postsUser);

      User userUpdated = userRepository.save(user);

    }
}