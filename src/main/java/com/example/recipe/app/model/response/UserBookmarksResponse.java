package com.example.recipe.app.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserBookmarksResponse {
    private Long userId;
    private List<RecipeResponse> recipes;
}
