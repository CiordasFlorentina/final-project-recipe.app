package com.example.recipe.app.controller;

import com.example.recipe.app.microservices.fault_tolerance.BulkheadService;
import com.example.recipe.app.microservices.fault_tolerance.CircuitBreakerService;
import com.example.recipe.app.microservices.fault_tolerance.RetryingService;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientRequest;
import com.example.recipe.app.service.IngredientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RetryingService retryingService;

    @Autowired
    private BulkheadService bulkheadService;

    @Autowired
    private CircuitBreakerService circuitBreakerService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping()
    @ApiOperation(value = "GetAll", notes = "Get all ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() throws Exception {
//        List<Ingredient> listOfIngredients =  ingredientService.getIngredients();
//        List<Ingredient> listOfIngredients =  retryingService.getIngredientsTrowingException();
//        List<Ingredient> listOfIngredients = retryingService.getIngredientsIntervalFunction_Exponential();
//        List<Ingredient> listOfIngredients = retryingService.getIngredientsIntervalFunction_Random();
//        List<Ingredient> listOfIngredients = retryingService.getIngredientsAsyncRetryExample();
//        List<Ingredient> listOfIngredients = retryingService.getIngredientsRetryMetrics();
       // List<Ingredient> listOfIngredients = circuitBreakerService.GetIngredientsTimeoutError();
//        List<Ingredient> listOfIngredients = circuitBreakerService.countBasedSlidingWindow_FailedCalls();
        List<Ingredient> listOfIngredients = circuitBreakerService.countBasedSlidingWindow_SlowCalls();
        return new ResponseEntity<>(listOfIngredients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get", notes = "Get specific ingredient by id")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable Long id) {
        return new ResponseEntity<>(ingredientService.getIngredient(id), HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add", notes = "Add new ingredient")
    public ResponseEntity<Ingredient> addIngredient(@Valid @RequestBody IngredientRequest ingredient) throws Exception{
        Ingredient _ingredient = retryingService.addIngredientThrowingException();
        return new ResponseEntity<>(_ingredient, HttpStatus.OK);
    }
}
