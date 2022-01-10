package com.example.recipe.app.model.response;

import com.example.recipe.app.model.SettingsConfig.Language;
import com.example.recipe.app.model.SettingsConfig.UnitOfMeasure;
import com.example.recipe.app.model.SettingsConfig.UnitOfTemperature;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
public class UserSettingResponse {
    private Long userId;

    @Enumerated(value = EnumType.ORDINAL)
    private Language language;

    @Enumerated(value = EnumType.ORDINAL)
    private UnitOfMeasure unitOfMeasure;

    @Enumerated(value = EnumType.ORDINAL)
    private UnitOfTemperature unitOfTemperature;
}

