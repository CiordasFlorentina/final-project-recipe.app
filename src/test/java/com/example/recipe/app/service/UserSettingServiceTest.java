package com.example.recipe.app.service;

import com.example.recipe.app.exeption.ElementPresentException;
import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.SettingsConfig.Language;
import com.example.recipe.app.model.SettingsConfig.UnitOfMeasure;
import com.example.recipe.app.model.SettingsConfig.UnitOfTemperature;
import com.example.recipe.app.model.entity.User;
import com.example.recipe.app.model.entity.UserSetting;
import com.example.recipe.app.model.request.UserSettingRequest;
import com.example.recipe.app.model.response.UserSettingResponse;
import com.example.recipe.app.repository.UserSettingRepository;
import com.example.recipe.app.service.UserService;
import com.example.recipe.app.service.UserSettingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@WebMvcTest(UserSettingService.class)
class UserSettingServiceTest {

    @MockBean
    private UserSettingRepository userSettingRepository;

    @MockBean
    UserService userService;

    private UserSettingService userSettingService;

    @BeforeEach
    void setUp() {
        userSettingService = new UserSettingService(userSettingRepository, userService);
    }

    private final UserSettingResponse userSettingResponse = UserSettingResponse.builder().userId(1L).language(Language.English)
            .unitOfTemperature(UnitOfTemperature.Celsius).unitOfMeasure(UnitOfMeasure.Metric).build();
    private final UserSettingRequest userSettingRequest = UserSettingRequest.builder().language(Language.English)
            .unitOfTemperature(UnitOfTemperature.Celsius).unitOfMeasure(UnitOfMeasure.Metric).build();
    private final User user = User.builder().name("User 1").id(1L).email("user@gmail.com").build();
    private final UserSetting userSetting = UserSetting.builder().user(user).id(1L).language(Language.English)
            .unitOfMeasure(UnitOfMeasure.Metric).unitOfTemperature(UnitOfTemperature.Celsius).build();


    @Test
    void getUserSettingsNotFound() {
        doReturn(Optional.empty()).when(userSettingRepository).findByUserId(1L);

        assertThrows(NotFoundException.class, () -> userSettingService.getUserSettings(1L));
    }

    @Test
    void getUserSettings() {
        doReturn(Optional.of(userSetting)).when(userSettingRepository).findByUserId(1L);

        UserSettingResponse result = userSettingService.getUserSettings(1L);

        assertEquals(userSettingResponse.getUserId(), result.getUserId());
        assertEquals(userSettingResponse.getLanguage().name(), result.getLanguage().name());
        assertEquals(userSettingResponse.getUnitOfMeasure().name(), result.getUnitOfMeasure().name());
        assertEquals(userSettingResponse.getUnitOfTemperature().name(), result.getUnitOfTemperature().name());
    }

    @Test
    void addUserSettingElementPresent() {
        doReturn(Optional.of(userSetting)).when(userSettingRepository).findByUserId(1L);

        assertThrows(ElementPresentException.class, () -> userSettingService.addUserSetting(1L, userSettingRequest));
    }

    @Test
    void addUserSetting() {
        doReturn(Optional.empty()).when(userSettingRepository).findByUserId(1L);
        doReturn(userSetting).when(userSettingRepository).save(any());
        doReturn(user).when(userService).getUser(1L);

        UserSettingResponse result = userSettingService.addUserSetting(1L, userSettingRequest);

        assertEquals(userSettingResponse.getUserId(), result.getUserId());
        assertEquals(userSettingResponse.getLanguage().name(), result.getLanguage().name());
        assertEquals(userSettingResponse.getUnitOfMeasure().name(), result.getUnitOfMeasure().name());
        assertEquals(userSettingResponse.getUnitOfTemperature().name(), result.getUnitOfTemperature().name());
    }
}