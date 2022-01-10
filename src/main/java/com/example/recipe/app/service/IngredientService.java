package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient getIngredient(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No ingredient with id " + id + " found"));
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient addIngredient(String ingredientName) {
        Optional<Ingredient> ingredient = ingredientRepository.findByName(ingredientName);
        if (ingredient.isEmpty()) {
            return ingredientRepository.save(Ingredient.builder().name(ingredientName).build());
        }

        return ingredient.get();
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


}
