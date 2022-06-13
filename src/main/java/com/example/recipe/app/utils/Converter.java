package com.example.recipe.app.utils;

import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.entity.UserSetting;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.model.response.RecipeIngredientResponse;
import com.example.recipe.app.model.response.RecipeResponse;
import com.example.recipe.app.model.response.UserSettingResponse;

import java.util.stream.Collectors;

public class Converter {
    public static FullRecipeResponse mapToResponse(Recipe recipe) {
        return FullRecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .ingredients(recipe.getRecipeIngredients()
                        .stream()
                        .map(ing -> RecipeIngredientResponse.builder()
                                .ingredient(ing.getIngredient())
                                .quantity(ing.getQuantity()).build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static RecipeResponse mapToRecipeResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .build();
    }

    public static UserSettingResponse mapToResponse(UserSetting userSetting) {
        return UserSettingResponse.builder()
                .userId(userSetting.getUser().getId())
                .language(userSetting.getLanguage())
                .unitOfMeasure(userSetting.getUnitOfMeasure())
                .unitOfTemperature(userSetting.getUnitOfTemperature())
                .build();
    }

}
