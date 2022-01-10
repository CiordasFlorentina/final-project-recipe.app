package com.example.recipe.app.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class RecipeRequest {
    @NotNull(message = "can not be null")
    private String name;

    @Valid
    private List<IngredientWithQuantity> ingredientsWithQuantity;
}
