package com.example.recipe.app.service;

import com.example.recipe.app.exeption.ElementPresentException;
import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.UserSetting;
import com.example.recipe.app.model.request.UserSettingRequest;
import com.example.recipe.app.model.response.UserSettingResponse;
import com.example.recipe.app.repository.UserSettingRepository;
import com.example.recipe.app.utils.Converter;
import org.springframework.stereotype.Service;

@Service
public class UserSettingService {
    private final UserSettingRepository userSettingRepository;
    private final UserService userService;

    UserSettingService(UserSettingRepository userSettingRepository, UserService userService) {
        this.userSettingRepository = userSettingRepository;
        this.userService = userService;
    }


    public UserSettingResponse getUserSettings(Long userId) {
        return Converter.mapToResponse(userSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No user settings for user with id " + userId + " found"))
        );
    }

    public UserSettingResponse addUserSetting(Long userId, UserSettingRequest userSetting) {
        if (userSettingRepository.findByUserId(userId).isPresent()) {
            throw new ElementPresentException("User Setting");
        }
        return Converter.mapToResponse(userSettingRepository.save(
                new UserSetting(
                        userService.getUser(userId),
                        userSetting.getLanguage(),
                        userSetting.getUnitOfMeasure(),
                        userSetting.getUnitOfTemperature()
                ))
        );

    }

}
