package com.example.recipe.app.model.request;

import com.example.recipe.app.model.SettingsConfig.Language;
import com.example.recipe.app.model.SettingsConfig.UnitOfMeasure;
import com.example.recipe.app.model.SettingsConfig.UnitOfTemperature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingRequest {
    private Language language;

    private UnitOfMeasure unitOfMeasure;

    private UnitOfTemperature unitOfTemperature;
}
