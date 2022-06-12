package com.example.recipe.app.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Schema
@Builder
public class IngredientWithQuantity {
    @Size(min = 3, message = "must be at least 3 characters long")
    @NotNull
    @Schema(description ="ingredient name", required = true)
    private String name;

    @Schema(description ="ingredient quantity")
    private String quantity;
}
