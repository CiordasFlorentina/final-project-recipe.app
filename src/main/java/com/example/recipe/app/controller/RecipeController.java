package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.request.IngredientWithQuantity;
import com.example.recipe.app.model.request.RecipeRequest;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.service.BookmarkService;
import com.example.recipe.app.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@Tag(name = "Recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping()
    @Operation(method = "GetAll", description = "Get all recipes")
    public List<FullRecipeResponse> getRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/{id}")
    @Operation(method = "Get", description = "Get specific recipe by id")
    public FullRecipeResponse getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping()
    @Operation(method = "Add", description = "Add new recipe")
    @ResponseStatus(HttpStatus.CREATED)
    public FullRecipeResponse addRecipe(@Valid @RequestBody RecipeRequest recipe) {
        return recipeService.addRecipe(recipe);
    }

    @PutMapping("/{id}")
    @Operation(method = "Update", description = "Update recipe details and ingredient list")
    public FullRecipeResponse updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeRequest recipe) {
        return recipeService.updateRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(method = "Delete", description = "Delete recipe")
    public ResponseEntity<Long> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/addIngredient")
    @Operation(method = "AddIngredient", description = "Add new ingredient with quantity to the recipe")
    public FullRecipeResponse addIngredient(@PathVariable Long id, @Valid @RequestBody IngredientWithQuantity ingredientWithQuantity) {
        return recipeService.addIngredientToRecipe(id, ingredientWithQuantity);
    }

    @DeleteMapping("/{id}/removeIngredient/{ingredientId}")
    @Operation(method = "RemoveIngredient", description = "Remove ingredient from recipe")
    public FullRecipeResponse removeIngredient(@PathVariable Long id, @PathVariable Long ingredientId) {
        return recipeService.removeIngredient(id, ingredientId);
    }
}
