package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.User;
import com.example.recipe.app.model.request.UserRequest;
import com.example.recipe.app.repository.UserRepository;
import com.example.recipe.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@WebMvcTest(UserService.class)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    private final User user = User.builder().name("User 1").id(1L).email("user@gmail.com").build();
    private final UserRequest userRequest = UserRequest.builder().email("user@gmail.com").name("User 1").build();


    @Test
    void getUsers() {
        doReturn(List.of(user)).when(userRepository).findAll();

        List<User> result = userService.getUsers();

        assertEquals(user.getId(), result.get(0).getId());
        assertEquals(user.getName(), result.get(0).getName());
        assertEquals(user.getEmail(), result.get(0).getEmail());
    }

    @Test
    void getUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(1L);

        assertThrows(NotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void getUser() {
        doReturn(Optional.of(user)).when(userRepository).findById(1L);

        User result = userService.getUser(1L);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void addUser() {
        doReturn(user).when(userRepository).save(any());

        User result = userService.addUser(userRequest);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void updateUserConstrainViolation() {
        final User modifiedUser = User.builder().name("User 2").id(1L).email("user2@gmail.com").build();
        doReturn(Optional.of(modifiedUser)).when(userRepository).findById(1L);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(any());

        assertThrows(ConstraintViolationException.class, () -> userService.updateUser(1L, userRequest));
    }

    @Test
    void updateUser() {
        final User modifiedUser = User.builder().name("Uuser").id(1L).email("user@gmail.com").build();
        doReturn(Optional.of(modifiedUser)).when(userRepository).findById(1L);
        doReturn(Optional.empty()).when(userRepository).findByEmail(any());
        doReturn(user).when(userRepository).save(any());

        User result = userService.updateUser(1L, userRequest);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }
}