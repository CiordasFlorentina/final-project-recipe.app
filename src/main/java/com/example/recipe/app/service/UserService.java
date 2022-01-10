package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.User;
import com.example.recipe.app.model.request.UserRequest;
import com.example.recipe.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("No user with id " + id + " found"));
    }

    public User addUser(UserRequest user) {
        return userRepository.save(
                User.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .build()
        );
    }

    public User updateUser(Long id, UserRequest newUser) {
        User user = getUser(id);
        if (!user.getEmail().equals(newUser.getEmail()) && userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new ConstraintViolationException(null);
        }
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        return userRepository.save(user);
    }
}
