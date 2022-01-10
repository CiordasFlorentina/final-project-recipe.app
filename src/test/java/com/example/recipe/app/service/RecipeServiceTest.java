package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.entity.RecipeIngredient;
import com.example.recipe.app.model.request.IngredientWithQuantity;
import com.example.recipe.app.model.request.RecipeRequest;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(RecipeService.class)
class RecipeServiceTest {

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    IngredientService ingredientService;

    @MockBean
    RecipeIngredientService recipeIngredientService;

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeService(recipeRepository, ingredientService, recipeIngredientService);
    }

    private final Recipe recipe = Recipe.builder().id(1L).name("rep 1").build();
    private final Ingredient ingredient = Ingredient.builder().id(1L).name("ing 1").build();
    private final Ingredient ingredient2 = Ingredient.builder().name("ing 2").build();
    private final RecipeIngredient recipeIngredient = RecipeIngredient
            .builder()
            .recipe(recipe)
            .ingredient(ingredient)
            .quantity("1 gram")
            .build();
    {
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(recipeIngredient);
        recipe.setRecipeIngredients(recipeIngredients);
    }


    @Test
    void getRecipe() {
        doReturn(Optional.of(recipe)).when(recipeRepository).findById(1L);

        FullRecipeResponse fullRecipeResponse = recipeService.getRecipe(1L);

        assertEquals(recipe.getName(), fullRecipeResponse.getName());
        assertEquals(recipe.getId(), fullRecipeResponse.getId());
        assertEquals(recipeIngredient.getQuantity(), fullRecipeResponse.getIngredients().get(0).getQuantity());
        assertEquals(ingredient.getName(), fullRecipeResponse.getIngredients().get(0).getIngredient().getName());
    }

    @Test
    void getRecipeNotFound() {
        doReturn(Optional.empty()).when(recipeRepository).findById(1L);

        assertThrows(NotFoundException.class, () -> recipeService.getRecipe(1L));
    }

    @Test
    void getRecipes() {
        doReturn(List.of(recipe)).when(recipeRepository).findAll();

        List<FullRecipeResponse> fullRecipeRespons = recipeService.getRecipes();

        assertEquals(recipe.getName(), fullRecipeRespons.get(0).getName());
        assertEquals(recipe.getId(), fullRecipeRespons.get(0).getId());
        assertEquals(recipeIngredient.getQuantity(), fullRecipeRespons.get(0).getIngredients().get(0).getQuantity());
        assertEquals(ingredient.getName(), fullRecipeRespons.get(0).getIngredients().get(0).getIngredient().getName());
    }

    @Test
    void addRecipe() {
        RecipeRequest recipeRequest = RecipeRequest.builder().name("rep 1").build();
        recipe.setRecipeIngredients(new ArrayList<>());

        doReturn(List.of(recipe)).when(recipeRepository).findAll();
        doReturn(List.of(ingredient)).when(ingredientService).addIngredients(any());
        doReturn(recipe).when(recipeRepository).save(any());

        FullRecipeResponse fullRecipeResponse = recipeService.addRecipe(recipeRequest);

        assertEquals(recipe.getName(), fullRecipeResponse.getName());
        assertEquals(0, fullRecipeResponse.getIngredients().size());
    }

    @Test
    void updateRecipe() {
        RecipeRequest recipeRequestModified = RecipeRequest
                .builder()
                .name("rep 2")
                .ingredientsWithQuantity(List.of(IngredientWithQuantity.builder().quantity("1 gram").name("ing 2").build()))
                .build();

        doNothing().when(recipeIngredientService).deleteRecipeAssociations(any());
        doReturn(Optional.of(recipe)).when(recipeRepository).findById(any());
        doReturn(List.of(ingredient2)).when(ingredientService).addIngredients(any());
        doReturn(recipe).when(recipeRepository).save(any());

        FullRecipeResponse fullRecipeResponse = recipeService.updateRecipe(1L, recipeRequestModified);

        assertEquals(recipeRequestModified.getName(), fullRecipeResponse.getName());
        assertEquals(recipeRequestModified.getIngredientsWithQuantity().get(0).getQuantity(), fullRecipeResponse.getIngredients().get(0).getQuantity());
        assertEquals(recipeRequestModified.getIngredientsWithQuantity().get(0).getName(), fullRecipeResponse.getIngredients().get(0).getIngredient().getName());
    }

    @Test
    void deleteRecipe() {
        doNothing().when(recipeRepository).deleteById(any());

        recipeService.deleteRecipe(1L);

        verify(recipeRepository, times(1)).deleteById(any());
    }

    @Test
    void addIngredientToRecipe() {
        IngredientWithQuantity ingredientWithQuantity = IngredientWithQuantity.builder().quantity("1 gram").name("ing 2").build();

        doReturn(Optional.of(recipe)).when(recipeRepository).findById(any());
        doReturn(ingredient2).when(ingredientService).addIngredient(any());
        doReturn(recipe).when(recipeRepository).save(any());

        FullRecipeResponse fullRecipeResponse = recipeService.addIngredientToRecipe(1L, ingredientWithQuantity);

        assertEquals(recipe.getName(), fullRecipeResponse.getName());
        assertEquals(2, fullRecipeResponse.getIngredients().size());
        assertEquals(recipe.getRecipeIngredients().get(0).getQuantity(), fullRecipeResponse.getIngredients().get(0).getQuantity());
        assertEquals(recipe.getRecipeIngredients().get(0).getIngredient().getName(), fullRecipeResponse.getIngredients().get(0).getIngredient().getName());
        assertEquals(recipe.getRecipeIngredients().get(1).getQuantity(), fullRecipeResponse.getIngredients().get(1).getQuantity());
        assertEquals(recipe.getRecipeIngredients().get(1).getIngredient().getName(), fullRecipeResponse.getIngredients().get(1).getIngredient().getName());
    }

    @Test
    void removeIngredient() {
        doReturn(Optional.of(recipe)).when(recipeRepository).findById(any());
        doReturn(ingredient).when(ingredientService).getIngredient(any());
        doNothing().when(recipeIngredientService).deleteRecipeAssociation(any(), any());
        doReturn(recipe).when(recipeRepository).save(any());

        FullRecipeResponse fullRecipeResponse = recipeService.removeIngredient(1L, 1L);

        assertEquals(recipe.getName(), fullRecipeResponse.getName());
        assertTrue(fullRecipeResponse.getIngredients().isEmpty());

    }
}