package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.entity.RecipeIngredient;
import com.example.recipe.app.repository.RecipeIngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeIngredientService {
    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    void deleteRecipeAssociation(Recipe recipe, Ingredient ingredient) {
        RecipeIngredient association = recipeIngredientRepository.findRecipeIngredientByRecipeAndIngredient(recipe, ingredient)
                .orElseThrow(() -> new NotFoundException("No ingredient with id " + ingredient.getId() + " found"));
        recipeIngredientRepository.delete(association);
    }

    void deleteRecipeAssociations(List<RecipeIngredient> recipeIds) {
        recipeIngredientRepository.deleteAllInBatch(recipeIds);
    }
}
