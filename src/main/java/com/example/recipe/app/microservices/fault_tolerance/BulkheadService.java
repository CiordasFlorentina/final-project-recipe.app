package com.example.recipe.app.microservices.fault_tolerance;

import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.service.IngredientService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BulkheadService {

    @Autowired
    private IngredientService ingredientService;


    private final String INGREDIENTS_PROPERTY = "IngredientService"; //used in application.yml


    @Bulkhead(name=INGREDIENTS_PROPERTY, fallbackMethod = "ingredientBulkHeadFallback")
    public List<Ingredient> getAllIngredientsConcurrentCalls(){
        return Collections.emptyList();
    }

    public List<Ingredient> ingredientBulkHeadFallback(Exception e){
        System.out.println("IngredientService is full and does not permit further calls!");
       return Collections.emptyList();
    }

}
