package com.example.assurancehabitation.controller;

import com.example.assurancehabitation.dto.user.UserRequest;
import com.example.assurancehabitation.dto.user.UserResponse;
import com.example.assurancehabitation.exception.GlobalExceptionHandler;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @Test
    void createUser_shouldReturn201() throws Exception {
        UserRequest request = new UserRequest("Alice Dupont", "alice@example.com", "+33612345678");
        UserResponse response = new UserResponse(1L, "Alice Dupont", "alice@example.com", "+33612345678", LocalDateTime.now());

        when(userService.create(any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void createUser_invalidEmail_shouldReturn400() throws Exception {
        UserRequest request = new UserRequest("Alice", "not-an-email", "+33612345678");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getUser_shouldReturn200() throws Exception {
        UserResponse response = new UserResponse(1L, "Alice Dupont", "alice@example.com", "+33612345678", LocalDateTime.now());
        when(userService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Dupont"));
    }

    @Test
    void getUser_notFound_shouldReturn404() throws Exception {
        when(userService.findById(99L)).thenThrow(new ResourceNotFoundException("User not found with id: 99"));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getAllUsers_shouldReturn200WithList() throws Exception {
        when(userService.findAll()).thenReturn(List.of(
                new UserResponse(1L, "Alice", "alice@example.com", "+33611111111", LocalDateTime.now()),
                new UserResponse(2L, "Bob", "bob@example.com", "+33622222222", LocalDateTime.now())
        ));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_notFound_shouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("User not found with id: 99"))
                .when(userService).delete(99L);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound());
    }
}
