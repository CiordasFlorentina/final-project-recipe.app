package com.example.recipe.app.utils;

import com.example.recipe.app.model.SettingsConfig.Language;
import com.example.recipe.app.model.SettingsConfig.UnitOfMeasure;
import com.example.recipe.app.model.SettingsConfig.UnitOfTemperature;
import com.example.recipe.app.model.entity.*;
import com.example.recipe.app.model.response.BookmarkResponse;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.model.response.UserSettingResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterTest {

    private final Recipe recipe = Recipe.builder().id(1L).name("recipe 1").build();
    private final User user = User.builder().name("User 1").id(1L).email("user@gmail.com").build();


    @Test
    void mapToResponseRecipe() {
        final RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(Ingredient.builder().name("ing 1").build())
                .quantity("1 gram")
                .build();
        recipe.setRecipeIngredients(List.of(recipeIngredient));
        FullRecipeResponse fullRecipeResponse = Converter.mapToResponse(recipe);

        assertEquals(1L, fullRecipeResponse.getId());
        assertEquals("recipe 1", fullRecipeResponse.getName());
        assertEquals("1 gram", fullRecipeResponse.getIngredients().get(0).getQuantity());
        assertEquals("ing 1", fullRecipeResponse.getIngredients().get(0).getIngredient().getName());
    }

    @Test
    void mapToResponseBookmark() {
        LocalDateTime now = LocalDateTime.now();
        final Bookmark bookmark = Bookmark.builder().id(1L).recipe(recipe).user(user).timestamp(now).build();
        BookmarkResponse fullRecipeResponse = Converter.mapToResponse(bookmark);

        assertEquals(1L, fullRecipeResponse.getId());
        assertEquals(1L, fullRecipeResponse.getRecipeId());
        assertEquals(1L, fullRecipeResponse.getUserId());
        assertEquals(now.toString(), fullRecipeResponse.getTimestamp());
    }

    @Test
    void mapToResponseUserSetting() {
        final UserSetting userSetting = UserSetting.builder().id(1L).user(user).language(Language.English)
                .unitOfTemperature(UnitOfTemperature.Celsius).unitOfMeasure(UnitOfMeasure.Metric).build();
        UserSettingResponse fullRecipeResponse = Converter.mapToResponse(userSetting);

        assertEquals(1L, fullRecipeResponse.getUserId());
        assertEquals(UnitOfTemperature.Celsius, fullRecipeResponse.getUnitOfTemperature());
        assertEquals(UnitOfMeasure.Metric, fullRecipeResponse.getUnitOfMeasure());
        assertEquals(Language.English, fullRecipeResponse.getLanguage());
    }
}