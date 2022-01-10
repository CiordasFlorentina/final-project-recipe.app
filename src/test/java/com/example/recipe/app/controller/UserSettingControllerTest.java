package com.example.recipe.app.controller;

import com.example.recipe.app.model.SettingsConfig.Language;
import com.example.recipe.app.model.SettingsConfig.UnitOfMeasure;
import com.example.recipe.app.model.SettingsConfig.UnitOfTemperature;
import com.example.recipe.app.model.request.UserSettingRequest;
import com.example.recipe.app.model.response.UserSettingResponse;
import com.example.recipe.app.service.UserSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserSettingController.class)
class UserSettingControllerTest {

    @MockBean
    private UserSettingService service;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserSettingResponse userSettingResponse = UserSettingResponse.builder().userId(1L).language(Language.English)
            .unitOfMeasure(UnitOfMeasure.Metric).unitOfTemperature(UnitOfTemperature.Celsius).build();

    @Test
    @DisplayName("GET /user-setting/{id}")
    void getUserSettings() throws Exception {
        doReturn(userSettingResponse).when(service).getUserSettings(1L);

        mockMvc.perform(get("/user-setting/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.language").value("English"))
                .andExpect(jsonPath("$.unitOfMeasure").value("Metric"))
                .andExpect(jsonPath("$.unitOfTemperature").value("Celsius"));
    }

    @Test
    @DisplayName("POST /user-setting/{id}")
    void addUserSetting() throws Exception {
        UserSettingRequest userSettingRequest = UserSettingRequest.builder().language(Language.English)
                .unitOfTemperature(UnitOfTemperature.Celsius).unitOfMeasure(UnitOfMeasure.Metric).build();

        doReturn(userSettingResponse).when(service).addUserSetting(1L, userSettingRequest);

        mockMvc.perform(
                        post("/user-setting/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userSettingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.language").value("English"))
                .andExpect(jsonPath("$.unitOfMeasure").value("Metric"))
                .andExpect(jsonPath("$.unitOfTemperature").value("Celsius"));
    }
}