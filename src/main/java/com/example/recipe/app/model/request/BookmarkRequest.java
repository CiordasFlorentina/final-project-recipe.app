package com.example.recipe.app.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class BookmarkRequest {
    @NotNull(message = "can not be null")
    private Long userId;

    @NotNull(message = "can not be null")
    private Long recipeId;
}
