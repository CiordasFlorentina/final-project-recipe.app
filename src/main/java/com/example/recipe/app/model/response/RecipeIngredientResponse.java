package com.example.recipe.app.model.response;

import com.example.recipe.app.model.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RecipeIngredientResponse {
    private Ingredient ingredient;
    private String quantity;

}
