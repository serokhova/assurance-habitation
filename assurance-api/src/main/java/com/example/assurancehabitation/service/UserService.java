package com.example.assurancehabitation.service;

import com.example.assurancehabitation.dto.user.UserRequest;
import com.example.assurancehabitation.dto.user.UserResponse;
import com.example.assurancehabitation.entity.assurance.User;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.messaging.producer.JmsEventProducer;
import com.example.assurancehabitation.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private static final String QUEUE_USER_CREATED = "user.created.queue";

    private final UserRepository userRepository;
    private final JmsEventProducer jmsEventProducer;

    public UserService(UserRepository userRepository, JmsEventProducer jmsEventProducer) {
        this.userRepository = userRepository;
        this.jmsEventProducer = jmsEventProducer;
    }

    public UserResponse create(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        User saved = userRepository.save(user);

        // Event Message pattern: publish USER_CREATED after persistence
        // Message Translator: UserResponse is translated into a JSON message by JmsEventProducer
        jmsEventProducer.publish(QUEUE_USER_CREATED, "USER_CREATED", toResponse(saved));

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public UserResponse update(Long id, UserRequest request) {
        User user = findEntityById(id);
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt()
        );
    }
}
