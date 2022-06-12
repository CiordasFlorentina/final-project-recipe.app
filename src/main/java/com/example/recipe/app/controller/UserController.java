package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.User;
import com.example.recipe.app.model.request.UserRequest;
import com.example.recipe.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @Operation(summary ="GetAll", description ="Get all users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary ="Get", description ="Get user by id")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary ="Add", description ="Add new user")
    public User addUser(@Valid @RequestBody UserRequest user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary ="Update", description ="Update user details")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest user) {
        return userService.updateUser(id, user);
    }

}
