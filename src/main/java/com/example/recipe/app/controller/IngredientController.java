package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientRequest;
import com.example.recipe.app.service.IngredientService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ingredient")
@Api(tags = "Ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    private static final String INGREDIENT_SERVICE = "IngredientService";

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping()
    @ApiOperation(value = "GetAll", notes = "Get all ingredients")
    public List<Ingredient> getIngredients() {
        return ingredientService.getIngredients();
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name=INGREDIENT_SERVICE, fallbackMethod="ingredientFallback")
    @ApiOperation(value = "Get", notes = "Get specific ingredient by id")
    public Ingredient getIngredient(@PathVariable Long id) throws Exception {
        if(true) {
            throw new Exception("Exception message"); // Checking CircuitBreaker..
        }
        return ingredientService.getIngredient(id);
    }

    public String ingredientFallback(Exception e){
        return new String("Server is down");
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add", notes = "Add new ingredient")
    public Ingredient addIngredient(@Valid @RequestBody IngredientRequest ingredient) {
        return ingredientService.addIngredient(ingredient.getName());
    }

}
