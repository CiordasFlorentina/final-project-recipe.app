package com.example.recipe.app.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkResponse {
    private Long id;
    private String timestamp;
    private Long userId;
    private Long recipeId;

}
