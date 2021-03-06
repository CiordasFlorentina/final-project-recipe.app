package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientRequest;
import com.example.recipe.app.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ingredient")
@Tag(name = "Ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping()
    @Operation(method = "GetAll", description = "Get all ingredients")
    public List<Ingredient> getIngredients() {
        return ingredientService.getIngredients();
    }

    @GetMapping("/{id}")
    @Operation(method = "Get", description = "Get specific ingredient by id")
    public Ingredient getIngredient(@PathVariable Long id) {
        return ingredientService.getIngredient(id);
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "Add", description = "Add new ingredient")
    public Ingredient addIngredient(@RequestBody @Valid IngredientRequest ingredient) {
        return ingredientService.addIngredient(ingredient.getName());
    }

}
