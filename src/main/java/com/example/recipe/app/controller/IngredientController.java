package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientRequest;
import com.example.recipe.app.service.IngredientService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ingredient")
@Api(tags = "Ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public void fakeEndpointApiCall(){
        // Fake api call->throws exception->CircuitBreaker calls fallback function.
        // For validating the CircuitBreaker functionality
        restTemplate.getForObject("http://localhost:8081/ingredient", String.class);

    }

    private static final String INGREDIENT_SERVICE = "IngredientService";

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping()
    @CircuitBreaker(name=INGREDIENT_SERVICE, fallbackMethod="ingredientFallback")
    @ApiOperation(value = "GetAll", notes = "Get all ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
//        fakeEndpointApiCall();
        List<Ingredient> listOfIngredients =  ingredientService.getIngredients();
        return new ResponseEntity<List<Ingredient>>(listOfIngredients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name=INGREDIENT_SERVICE, fallbackMethod="ingredientFallback")
    @ApiOperation(value = "Get", notes = "Get specific ingredient by id")
    public ResponseEntity<String> getIngredient(@PathVariable Long id) {
//        fakeEndpointApiCall();
        String ingredient_to_string = ingredientService.getIngredient(id).toString();
        return new ResponseEntity<String>(ingredient_to_string, HttpStatus.OK);
    }

    @PostMapping()
    @CircuitBreaker(name=INGREDIENT_SERVICE, fallbackMethod="ingredientFallback")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add", notes = "Add new ingredient")
    public ResponseEntity<Ingredient> addIngredient(@Valid @RequestBody IngredientRequest ingredient) {
//        fakeEndpointApiCall();
        Ingredient _ingredient = ingredientService.addIngredient(ingredient.getName());
        return new ResponseEntity<>(_ingredient, HttpStatus.OK);
    }

    // Function called automatically when some errors occurred within Controller Api Calls
    // Must have the same return data type as the originally called functions
    public ResponseEntity<String> ingredientFallback(Exception e){
        return new ResponseEntity<String>("Ingredient Service is down!", HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
