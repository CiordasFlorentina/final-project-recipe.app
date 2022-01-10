package com.example.recipe.app.controller;

import com.example.recipe.app.model.request.IngredientWithQuantity;
import com.example.recipe.app.model.request.RecipeRequest;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@Api(tags = "Recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping()
    @ApiOperation(value = "GetAll", notes = "Get all recipes")
    public List<FullRecipeResponse> getRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get", notes = "Get specific recipe by id")
    public FullRecipeResponse getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping()
    @ApiOperation(value = "Add", notes = "Add new recipe")
    @ResponseStatus(HttpStatus.CREATED)
    public FullRecipeResponse addRecipe(@Valid @RequestBody RecipeRequest recipe) {
        return recipeService.addRecipe(recipe);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update", notes = "Update recipe details and ingredient list")
    public FullRecipeResponse updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeRequest recipe) {
        return recipeService.updateRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete", notes = "Delete recipe")
    public ResponseEntity<Long> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/addIngredient")
    @ApiOperation(value = "AddIngredient", notes = "Add new ingredient with quantity to the recipe")
    public FullRecipeResponse addIngredient(@PathVariable Long id, @Valid @RequestBody IngredientWithQuantity ingredientWithQuantity) {
        return recipeService.addIngredientToRecipe(id, ingredientWithQuantity);
    }

    @DeleteMapping("/{id}/removeIngredient/{ingredientId}")
    @ApiOperation(value = "RemoveIngredient", notes = "Remove ingredient from recipe")
    public FullRecipeResponse removeIngredient(@PathVariable Long id, @PathVariable Long ingredientId) {
        return recipeService.removeIngredient(id, ingredientId);
    }
}
