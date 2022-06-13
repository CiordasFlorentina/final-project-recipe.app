package com.example.recipe.app.model.request;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class IngredientWithQuantity {
    @Size(min = 3, message = "must be at least 3 characters long")
    @NotNull
    private String name;

    private String quantity;
}
