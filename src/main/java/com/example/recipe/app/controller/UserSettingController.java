package com.example.recipe.app.controller;

import com.example.recipe.app.model.request.UserSettingRequest;
import com.example.recipe.app.model.response.UserSettingResponse;
import com.example.recipe.app.service.UserSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user-setting")
@Api(tags = "User Settings")
public class UserSettingController {
    private final UserSettingService userSettingService;

    UserSettingController(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "Get", notes = "Get user settings")
    public UserSettingResponse getUserSettings(@PathVariable Long userId) {
        return userSettingService.getUserSettings(userId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Post", notes = "Add user settings")
    public UserSettingResponse addUserSetting(@PathVariable Long userId, @Valid @RequestBody UserSettingRequest userSetting) {
        return userSettingService.addUserSetting(userId, userSetting);
    }
}
