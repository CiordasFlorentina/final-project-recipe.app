package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.entity.Recipe;
import com.example.recipe.app.model.entity.RecipeIngredient;
import com.example.recipe.app.repository.RecipeIngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@WebMvcTest(RecipeIngredientService.class)
class RecipeIngredientServiceTest {

    @MockBean
    private RecipeIngredientRepository recipeIngredientRepository;

    private RecipeIngredientService recipeIngredientService;

    @BeforeEach
    void setup() {
        recipeIngredientService = new RecipeIngredientService(recipeIngredientRepository);
    }

    private final Recipe recipe = Recipe.builder().id(1L).name("rec 1").build();
    private final Ingredient ingredient = Ingredient.builder().name("ing 1").build();
    private final RecipeIngredient recipeIngredient = RecipeIngredient
            .builder()
            .recipe(recipe)
            .ingredient(ingredient)
            .quantity("qu 1")
            .build();


    @Test
    void deleteRecipeAssociation() {
        doReturn(Optional.of(recipeIngredient)).when(recipeIngredientRepository).findRecipeIngredientByRecipeAndIngredient(any(), any());
        doNothing().when(recipeIngredientRepository).delete(any());

        recipeIngredientService.deleteRecipeAssociation(recipe, ingredient);

        verify(recipeIngredientRepository, times(1)).delete(any());
    }

    @Test
    void notFoundDeleteRecipeAssociation() {
        doReturn(Optional.empty()).when(recipeIngredientRepository).findRecipeIngredientByRecipeAndIngredient(any(), any());

        assertThrows(NotFoundException.class, () -> recipeIngredientService.deleteRecipeAssociation(recipe, ingredient));
    }

    @Test
    void deleteRecipeAssociations() {
        doNothing().when(recipeIngredientRepository).deleteAll(any());

        recipeIngredientService.deleteRecipeAssociations(List.of(recipeIngredient));

        verify(recipeIngredientRepository, times(1)).deleteAllInBatch(any());
    }
}