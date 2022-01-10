package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.User;
import com.example.recipe.app.model.request.UserRequest;
import com.example.recipe.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final User user = User.builder().name("User 1").id(1L).email("user@gmail.com").build();
    private final UserRequest userRequest = UserRequest.builder().email("user@gmail.com").name("User 1").build();


    @Test
    @DisplayName("GET /user")
    void getUsers() throws Exception {
        doReturn(List.of(user)).when(service).getUsers();

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[0].email").value("user@gmail.com"));
    }

    @Test
    @DisplayName("GET /user/{id}")
    void getUser() throws Exception {
        doReturn(user).when(service).getUser(1L);

        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value("User 1"))
                .andExpect(jsonPath("$.email").value("user@gmail.com"));
    }

    @Test
    @DisplayName("Post /user")
    void addUser() throws Exception {
        doReturn(user).when(service).addUser(userRequest);

        mockMvc.perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("User 1"))
                .andExpect(jsonPath("$.email").value("user@gmail.com"));
    }

    @Test
    @DisplayName("PUT /user/{id}")
    void updateUser() throws Exception {
        doReturn(user).when(service).updateUser(1L, userRequest);

        mockMvc.perform(
                        put("/user/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User 1"))
                .andExpect(jsonPath("$.email").value("user@gmail.com"));
    }
}