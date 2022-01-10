package com.example.recipe.app.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FullRecipeResponse {
    private Long id;
    private String name;
    private List<RecipeIngredientResponse> ingredients;
}
