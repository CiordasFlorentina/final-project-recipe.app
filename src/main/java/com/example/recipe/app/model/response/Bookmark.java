package com.example.recipe.app.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Bookmark {
    private Long id;
    private String timestamp;
    private Long userId;
    private Long recipeId;

}

