package com.example.recipe.app.service;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@WebMvcTest(IngredientService.class)
public class IngredientServiceTest {

    @MockBean
    private IngredientRepository ingredientRepository;

    private IngredientService ingredientService;

    @BeforeEach
    void setup() {
        ingredientService = new IngredientService(ingredientRepository);
    }

    private final Ingredient ingredient1 = Ingredient.builder().name("ing 1").build();
    private final Ingredient ingredient2 = Ingredient.builder().name("ing 2").build();


    @Test
    void getIngredient() {
        doReturn(Optional.of(ingredient1)).when(ingredientRepository).findById(1L);

        Ingredient ingredient = ingredientService.getIngredient(1L);

        assertEquals("ing 1", ingredient.getName());
    }

    @Test
    void ingredientNotFound() {
        doReturn(Optional.empty()).when(ingredientRepository).findById(1L);

        assertThrows(NotFoundException.class, () -> ingredientService.getIngredient(1L));
    }

    @Test
    void getIngredients() {
        doReturn(List.of(ingredient1, ingredient2)).when(ingredientRepository).findAll();

        List<Ingredient> ingredients = ingredientService.getIngredients();

        assertEquals(2, ingredients.size());
        assertEquals("ing 1", ingredients.get(0).getName());
        assertEquals("ing 2", ingredients.get(1).getName());
    }

    @Test
    @DisplayName("should not add new ingredient when it is already present in datastore")
    void addIngredient() {
        doReturn(Optional.of(ingredient1)).when(ingredientRepository).findByName("ing 1");

        Ingredient ingredient = ingredientService.addIngredient("ing 1");

        verify(ingredientRepository, never()).save(any());
        assertEquals("ing 1", ingredient.getName());
    }

    @Test
    @DisplayName("should add new ingredient when it is not present in datastore")
    void addIngredientOne() {
        doReturn(List.of(ingredient1, ingredient2)).when(ingredientRepository).findAll();
        doReturn(Optional.empty()).when(ingredientRepository).findByName("ing 1");
        doReturn(ingredient1).when(ingredientRepository).save(any());

        Ingredient ingredient = ingredientService.addIngredient("ing 1");

        verify(ingredientRepository, times(1)).save(any());
        assertEquals("ing 1", ingredient.getName());
    }

    @Test
    void addIngredients() {
        doReturn(List.of(ingredient1)).when(ingredientRepository).findAll();
        doReturn(List.of(ingredient2)).when(ingredientRepository).saveAll(any());

        List<Ingredient> ingredients = ingredientService.addIngredients(List.of("ing 1", "ing 2"));

        verify(ingredientRepository, times(1)).saveAll(any());
        assertEquals(2, ingredients.size());
        assertEquals("ing 1", ingredients.get(0).getName());
        assertEquals("ing 2", ingredients.get(1).getName());
    }
}