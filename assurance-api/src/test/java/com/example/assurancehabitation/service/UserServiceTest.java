package com.example.assurancehabitation.service;

import com.example.assurancehabitation.config.TestJmsConfig;
import com.example.assurancehabitation.dto.user.UserRequest;
import com.example.assurancehabitation.dto.user.UserResponse;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.messaging.producer.JmsEventProducer;
import com.example.assurancehabitation.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestJmsConfig.class)
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private JmsEventProducer jmsEventProducer;

    @Test
    void createUser_shouldPersistUser() {
        UserRequest request = new UserRequest("Alice Dupont", "alice@test.com", "+33612345678");

        UserResponse response = userService.create(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Alice Dupont");
        assertThat(response.email()).isEqualTo("alice@test.com");
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    void createUser_shouldPublishJmsEvent() {
        UserRequest request = new UserRequest("Bob Martin", "bob@test.com", "+33698765432");

        userService.create(request);

        verify(jmsEventProducer).publish(eq("user.created.queue"), eq("USER_CREATED"), any());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        userService.create(new UserRequest("Alice", "alice2@test.com", "+33600000001"));
        userService.create(new UserRequest("Bob", "bob2@test.com", "+33600000002"));

        List<UserResponse> users = userService.findAll();

        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void findById_notFound_shouldThrowException() {
        assertThatThrownBy(() -> userService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void delete_shouldRemoveUser() {
        UserResponse created = userService.create(new UserRequest("Charlie", "charlie@test.com", "+33600000003"));

        userService.delete(created.id());

        assertThat(userRepository.findById(created.id())).isEmpty();
    }

    @Test
    void delete_notFound_shouldThrowException() {
        assertThatThrownBy(() -> userService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
