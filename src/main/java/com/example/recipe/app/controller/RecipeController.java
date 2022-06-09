package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientWithQuantity;
import com.example.recipe.app.model.request.RecipeRequest;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.service.IngredientService;
import com.example.recipe.app.service.RecipeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@Api(tags = "Recipe")
public class RecipeController {

    private final RecipeService recipeService;

    private static final String RECIPE_SERVICE = "RecipeService";

    @Autowired
    private RestTemplate restTemplate;

    public void fakeEndpointApiCall(){
        // Fake api call->throws exception->CircuitBreaker calls fallback function.
        // For validating the CircuitBreaker functionality
        restTemplate.getForObject("http://localhost:8081/recipe", String.class);
    }

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping()
    @CircuitBreaker(name=RECIPE_SERVICE, fallbackMethod="recipeFallBack")
    @ApiOperation(value = "GetAll", notes = "Get all recipes")
    public ResponseEntity<List<FullRecipeResponse>> getRecipes() {
//         fakeEndpointApiCall();
        return new ResponseEntity<>(recipeService.getRecipes(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    @CircuitBreaker(name=RECIPE_SERVICE, fallbackMethod="recipeFallBack")
    @ApiOperation(value = "Get", notes = "Get specific recipe by id")
    public ResponseEntity<FullRecipeResponse> getRecipe(@PathVariable Long id) {
//         fakeEndpointApiCall();
        return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.OK);
    }

    @PostMapping()
    @CircuitBreaker(name=RECIPE_SERVICE, fallbackMethod="recipeFallBack")
    @ApiOperation(value = "Add", notes = "Add new recipe")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FullRecipeResponse> addRecipe(@Valid @RequestBody RecipeRequest recipe) {
        // fakeEndpointApiCall();
        return new ResponseEntity<>(recipeService.addRecipe(recipe), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @CircuitBreaker(name=RECIPE_SERVICE, fallbackMethod="recipeFallBack")
    @ApiOperation(value = "Update", notes = "Update recipe details and ingredient list")
    public ResponseEntity<FullRecipeResponse> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeRequest recipe) {
        // fakeEndpointApiCall();
        return new ResponseEntity<>(recipeService.updateRecipe(id, recipe), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CircuitBreaker(name=RECIPE_SERVICE, fallbackMethod="recipeFallBack")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete", notes = "Delete recipe")
    public ResponseEntity<Long> deleteRecipe(@PathVariable Long id) {
        // fakeEndpointApiCall();
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/addIngredient")
    @CircuitBreaker(name=RECIPE_SERVICE, fallbackMethod="recipeFallBack")
    @ApiOperation(value = "AddIngredient", notes = "Add new ingredient with quantity to the recipe")
    public ResponseEntity<FullRecipeResponse> addIngredient(@PathVariable Long id, @Valid @RequestBody IngredientWithQuantity ingredientWithQuantity) {
        // fakeEndpointApiCall();
        return new ResponseEntity<>(recipeService.addIngredientToRecipe(id, ingredientWithQuantity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/removeIngredient/{ingredientId}")
    @ApiOperation(value = "RemoveIngredient", notes = "Remove ingredient from recipe")
    public ResponseEntity<FullRecipeResponse> removeIngredient(@PathVariable Long id, @PathVariable Long ingredientId) {
        // fakeEndpointApiCall();
        return new ResponseEntity<>(recipeService.removeIngredient(id, ingredientId), HttpStatus.OK);
    }


    public ResponseEntity<String> recipeFallBack(Exception e){
        return new ResponseEntity<String>("Recipe Service is down!", HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
