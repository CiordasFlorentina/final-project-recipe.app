package com.example.recipe.app.controller;

import com.example.recipe.app.model.request.IngredientWithQuantity;
import com.example.recipe.app.model.request.RecipeRequest;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.service.RecipeService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@Tag(name = "Recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService ) {
        this.recipeService = recipeService;
    }

    @GetMapping()
    @Operation(summary ="GetAll", description ="Get all recipes")
    public ResponseEntity<List<FullRecipeResponse>> getRecipes() {
        return new ResponseEntity<>(recipeService.getRecipes(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    @Operation(summary ="Get", description ="Get specific recipe by id")
    public ResponseEntity<FullRecipeResponse> getRecipe(@PathVariable Long id) {
        return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.OK);
    }

    @PostMapping()
    @Operation(summary ="Add", description ="Add new recipe")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FullRecipeResponse> addRecipe(@Valid @RequestBody RecipeRequest recipe) {
        return new ResponseEntity<>(recipeService.addRecipe(recipe), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary ="Update", description ="Update recipe details and ingredient list")
    public ResponseEntity<FullRecipeResponse> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeRequest recipe) {
        return new ResponseEntity<>(recipeService.updateRecipe(id, recipe), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary ="Delete", description ="Delete recipe")
    public ResponseEntity<Long> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/addIngredient")
    @Operation(summary ="AddIngredient", description ="Add new ingredient with quantity to the recipe")
    public ResponseEntity<FullRecipeResponse> addIngredient(@PathVariable Long id, @Valid @RequestBody IngredientWithQuantity ingredientWithQuantity) {
        return new ResponseEntity<>(recipeService.addIngredientToRecipe(id, ingredientWithQuantity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/removeIngredient/{ingredientId}")
    @Operation(summary ="RemoveIngredient", description ="Remove ingredient from recipe")
    public ResponseEntity<FullRecipeResponse> removeIngredient(@PathVariable Long id, @PathVariable Long ingredientId) {
        return new ResponseEntity<>(recipeService.removeIngredient(id, ingredientId), HttpStatus.OK);
    }


    public ResponseEntity<String> recipeFallBack(Exception e){
        return new ResponseEntity<String>("Recipe Service is down!", HttpStatus.INTERNAL_SERVER_ERROR);

    }
    public ResponseEntity<String> recipeBulkHeadFallback(Exception e){
        return new ResponseEntity<>("RecipeService is full and does not permit further calls!", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
