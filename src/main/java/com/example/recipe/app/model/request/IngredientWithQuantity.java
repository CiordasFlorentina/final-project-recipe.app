package com.example.recipe.app.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel
@Builder
public class IngredientWithQuantity {
    @Size(min = 3, message = "must be at least 3 characters long")
    @NotNull
    @ApiModelProperty(notes = "ingredient name", required = true)
    private String name;

    @ApiModelProperty(notes = "ingredient quantity")
    private String quantity;
}
