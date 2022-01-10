package com.example.recipe.app.model.entity;

import com.example.recipe.app.model.SettingsConfig.Language;
import com.example.recipe.app.model.SettingsConfig.UnitOfMeasure;
import com.example.recipe.app.model.SettingsConfig.UnitOfTemperature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private Language language = Language.English;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private UnitOfMeasure unitOfMeasure = UnitOfMeasure.Metric;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private UnitOfTemperature unitOfTemperature = UnitOfTemperature.Celsius;

    public UserSetting(User user, Language language, UnitOfMeasure unitOfMeasure, UnitOfTemperature unitOfTemperature) {
        this.user = user;
        if (language != null) {
            this.language = language;
        }
        if (unitOfMeasure != null) {
            this.unitOfMeasure = unitOfMeasure;
        }
        if (unitOfTemperature != null) {
            this.unitOfTemperature = unitOfTemperature;
        }

    }
}
