package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientRequest;
import com.example.recipe.app.service.IngredientService;
import com.example.recipe.app.utils.FaultTolerance;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ingredient")
@Api(tags = "Ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    private static final String INGREDIENT_SERVICE = "IngredientService";

    private final FaultTolerance faultTolerance;

    public IngredientController(IngredientService ingredientService, FaultTolerance faultTolerance) {
        this.ingredientService = ingredientService;
        this.faultTolerance = faultTolerance;
    }

    @GetMapping()
    @CircuitBreaker(name=INGREDIENT_SERVICE, fallbackMethod="ingredientFallback")
    @Bulkhead(name=INGREDIENT_SERVICE, fallbackMethod = "ingredientBulkHeadFallback")
    @ApiOperation(value = "GetAll", notes = "Get all ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        faultTolerance.concurrentRequestsApiCall();
        faultTolerance.fakeEndpointApiCall();
        List<Ingredient> listOfIngredients =  ingredientService.getIngredients();
        return new ResponseEntity<List<Ingredient>>(listOfIngredients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name=INGREDIENT_SERVICE, fallbackMethod="ingredientFallback")
    @Bulkhead(name=INGREDIENT_SERVICE, fallbackMethod = "ingredientBulkHeadFallback")
    @ApiOperation(value = "Get", notes = "Get specific ingredient by id")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable Long id) {
        return new ResponseEntity<>(ingredientService.getIngredient(id), HttpStatus.OK);
    }

    @PostMapping()
    @CircuitBreaker(name=INGREDIENT_SERVICE, fallbackMethod="ingredientFallback")
    @Bulkhead(name=INGREDIENT_SERVICE, fallbackMethod = "ingredientBulkHeadFallback")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add", notes = "Add new ingredient")
    public ResponseEntity<Ingredient> addIngredient(@Valid @RequestBody IngredientRequest ingredient) {
        Ingredient _ingredient = ingredientService.addIngredient(ingredient.getName());
        return new ResponseEntity<>(_ingredient, HttpStatus.OK);
    }

    // Function called automatically when some errors occurred within Controller Api Calls
    // Must have the same return data type as the originally called functions
    public ResponseEntity<String> ingredientFallback(Exception e){
        return new ResponseEntity<>("Ingredient Service is down!", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<String> ingredientBulkHeadFallback(Exception e){
        return new ResponseEntity<>("IngredientService is full and does not permit further calls!", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
