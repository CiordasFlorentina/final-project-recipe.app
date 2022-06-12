package com.example.recipe.app.controller;

import com.example.recipe.app.microservices.fault_tolerance.BulkheadService;
import com.example.recipe.app.microservices.fault_tolerance.CircuitBreakerService;
import com.example.recipe.app.microservices.fault_tolerance.RetryingService;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientRequest;
import com.example.recipe.app.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ingredient")
@Tag(name = "Ingredient")
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
    @Operation(summary ="GetAll", description ="Get all ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() throws Exception {
          List<Ingredient> listOfIngredients =  ingredientService.getIngredients();
//        List<Ingredient> listOfIngredients =  retryingService.getIngredientsTrowingException();
//        List<Ingredient> listOfIngredients = retryingService.getIngredientsIntervalFunction_Exponential();
//        List<Ingredient> listOfIngredients = retryingService.getIngredientsIntervalFunction_Random();
//        List<Ingredient> listOfIngredients = retryingService.getIngredientsAsyncRetryExample();
//        List<Ingredient> listOfIngredients = retryingService.getIngredientsRetryMetrics();
       // List<Ingredient> listOfIngredients = circuitBreakerService.GetIngredientsTimeoutError();
//        List<Ingredient> listOfIngredients = circuitBreakerService.countBasedSlidingWindow_FailedCalls();
//        List<Ingredient> listOfIngredients = circuitBreakerService.countBasedSlidingWindow_SlowCalls();
        //List<Ingredient> listOfIngredients = circuitBreakerService.getIngredientsCircuitBreakerEvents();
       //  List<Ingredient> listOfIngredients = circuitBreakerService.countBasedSlidingWindow_Failed_And_SlowCalls();
        //List<Ingredient> listOfIngredients = circuitBreakerService.circuitBreakerOpenAndThenClose();
        //List<Ingredient> listOfIngredients = circuitBreakerService.circuitBreakerFallback();
        //List<Ingredient> listOfIngredients = circuitBreakerService.getIngredientsCircuitBreakerMetrics();
//        List<Ingredient> listOfIngredients = bulkheadService.GetIngredientsSuccesfullyBulkhead();
        // List<Ingredient> listOfIngredients = bulkheadService.GetIngredientsExceptionByBulkhead();
        // List<Ingredient> listOfIngredients = bulkheadService.GetIngredientsEventsByBulkhead();
       //  List<Ingredient> listOfIngredients = bulkheadService.GetIngredientsMetricsByBulkhead();


        return new ResponseEntity<>(listOfIngredients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary ="Get", description ="Get specific ingredient by id")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable Long id) {
        return new ResponseEntity<>(ingredientService.getIngredient(id), HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary ="Add", description ="Add new ingredient")
    public ResponseEntity<Ingredient> addIngredient(@Valid @RequestBody IngredientRequest ingredient) throws Exception{
        Ingredient _ingredient = retryingService.addIngredientThrowingException(ingredient.getName());
        return new ResponseEntity<>(_ingredient, HttpStatus.OK);
    }
}
