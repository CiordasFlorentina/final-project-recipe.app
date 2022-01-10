package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.User;
import com.example.recipe.app.model.request.UserRequest;
import com.example.recipe.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "User")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @ApiOperation(value = "GetAll", notes = "Get all users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get", notes = "Get user by id")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add", notes = "Add new user")
    public User addUser(@Valid @RequestBody UserRequest user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update", notes = "Update user details")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest user) {
        return userService.updateUser(id, user);
    }

}
