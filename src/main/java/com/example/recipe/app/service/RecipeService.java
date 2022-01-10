package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.entity.RecipeIngredient;
import com.example.recipe.app.model.request.IngredientWithQuantity;
import com.example.recipe.app.model.request.RecipeRequest;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.repository.RecipeRepository;
import com.example.recipe.app.utils.Converter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final RecipeIngredientService recipeIngredientService;

    public RecipeService(
            RecipeRepository recipeRepository,
            IngredientService ingredientService,
            RecipeIngredientService recipeIngredientService
    ) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.recipeIngredientService = recipeIngredientService;
    }

    public List<FullRecipeResponse> getRecipes() {
        return recipeRepository.findAll().stream().map(Converter::mapToResponse).collect(Collectors.toList());
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No recipe with id " + id + " found"));
    }

    public FullRecipeResponse getRecipe(Long id) {
        return Converter.mapToResponse(getRecipeById(id));
    }

    public FullRecipeResponse addRecipe(RecipeRequest recipe) {
        Recipe newRecipe = Recipe.builder().name(recipe.getName()).build();

        newRecipe.setRecipeIngredients(getRecipeIngredients(recipe, newRecipe));
        return Converter.mapToResponse(recipeRepository.save(newRecipe));
    }

    public FullRecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) {
        Recipe recipe = getRecipeById(id);

        recipeIngredientService.deleteRecipeAssociations(recipe.getRecipeIngredients());
        recipe.setName(recipeRequest.getName());

        recipe.setRecipeIngredients(getRecipeIngredients(recipeRequest, recipe));
        return Converter.mapToResponse(recipeRepository.save(recipe));
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public FullRecipeResponse addIngredientToRecipe(Long recipeId, IngredientWithQuantity ingredientWithQuantity) {
        Recipe recipe = getRecipeById(recipeId);

        Ingredient ingredient = ingredientService.addIngredient(ingredientWithQuantity.getName());
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .quantity(ingredientWithQuantity.getQuantity())
                .build();
        recipe.getRecipeIngredients().add(recipeIngredient);

        return Converter.mapToResponse(recipeRepository.save(recipe));
    }

    @Transactional
    public FullRecipeResponse removeIngredient(Long recipeId, Long ingredientId) {
        Recipe recipe = getRecipeById(recipeId);
        Ingredient ingredient = ingredientService.getIngredient(ingredientId);

        recipeIngredientService.deleteRecipeAssociation(recipe, ingredient);
        recipe.getRecipeIngredients().removeIf((RecipeIngredient elm) -> elm.getIngredient().getId().equals(ingredientId));

        return Converter.mapToResponse(recipeRepository.save(recipe));
    }

    private List<RecipeIngredient> getRecipeIngredients(RecipeRequest recipe, Recipe newRecipe) {
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        if (recipe.getIngredientsWithQuantity() == null) {
            recipe.setIngredientsWithQuantity(new ArrayList<>());
        }

        List<String> recipeIngredientsNames = recipe.getIngredientsWithQuantity()
                .stream()
                .map(IngredientWithQuantity::getName)
                .collect(Collectors.toList());

        List<Ingredient> ingredients = ingredientService.addIngredients(recipeIngredientsNames);

        recipe.getIngredientsWithQuantity().forEach(elm -> {
            Optional<Ingredient> ing = ingredients
                    .stream()
                    .filter(i -> i.getName().equals(elm.getName()))
                    .findFirst();

            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .recipe(newRecipe)
                    .ingredient(ing.get())
                    .quantity(elm.getQuantity()).build();

            recipeIngredients.add(recipeIngredient);
        });

        return recipeIngredients;
    }

}
