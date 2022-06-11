package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.microservices.fault_tolerance.failures.NoDelay;
import com.example.recipe.app.microservices.fault_tolerance.failures.NoFailure;
import com.example.recipe.app.microservices.fault_tolerance.failures.PotentialDelay;
import com.example.recipe.app.microservices.fault_tolerance.failures.PotentialFailure;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    PotentialFailure potentialFailure = new NoFailure();

    PotentialDelay potentialDelay = new NoDelay();

    public void setPotentialDelay(PotentialDelay potentialDelay) {
        this.potentialDelay = potentialDelay;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient getIngredient(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No ingredient with id " + id + " found"));
    }

    public List<Ingredient> getIngredients() {
        this.potentialFailure.occur();
        this.potentialDelay.occur();
        return ingredientRepository.findAll();
    }

    public List<Ingredient> getIngredientsTimeoutError() {
        System.out.println("Get all ingredients in timeout Error function..."
                + "current time = " + LocalDateTime.now().format(formatter) +
                "; current thread = " + Thread.currentThread().getName());
        return Collections.emptyList();
    }


    public List<Ingredient> getIngredientsThrowingException() throws Exception {
        System.out.println("Get all ingredients; "
                + "current time = " + LocalDateTime.now().format(formatter) +
                "; current thread = " + Thread.currentThread().getName());

        throw new Exception("Exception when getting all ingredients");
    }

    public Ingredient addIngredient(String ingredientName) {
        Optional<Ingredient> ingredient = ingredientRepository.findByName(ingredientName);
        if (ingredient.isEmpty()) {
            return ingredientRepository.save(Ingredient.builder().name(ingredientName).build());
        }

        return ingredient.get();
    }

    public Ingredient addIngredientThrowingException() throws Exception {
        System.out.println("Adding ingredient... "
                + "current time = " + LocalDateTime.now().format(formatter) +
                "; current thread = " + Thread.currentThread().getName());

        throw new Exception("Exception when adding an ingredient...");
    }

    public List<Ingredient> addIngredients(List<String> ingredientNames) {
        List<Ingredient> recipeIngredients = new ArrayList<>();
        List<Ingredient> missingIngredients = new ArrayList<>();
        List<Ingredient> existingIngredients = ingredientRepository.findAll();

        ingredientNames.forEach(ingNames -> {
            Optional<Ingredient> existingIng = existingIngredients
                    .stream()
                    .filter(ing -> ing.getName().trim().toLowerCase().equals(ingNames))
                    .findFirst();
            if (existingIng.isPresent()) {
                recipeIngredients.add(existingIng.get());
            } else {
                missingIngredients.add(Ingredient.builder().name(ingNames).build());
            }
        });

        recipeIngredients.addAll(ingredientRepository.saveAll(missingIngredients));
        return recipeIngredients;
    }

    public void setPotentialFailure(PotentialFailure potentialFailure) {
        System.out.println("Setting potential failure..");
        this.potentialFailure = potentialFailure;
    }

}
