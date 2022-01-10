package com.example.recipe.app.controller;

import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientWithQuantity;
import com.example.recipe.app.model.request.RecipeRequest;
import com.example.recipe.app.model.response.FullRecipeResponse;
import com.example.recipe.app.model.response.RecipeIngredientResponse;
import com.example.recipe.app.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @MockBean
    private RecipeService service;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final FullRecipeResponse recipeWithOneIngredient = FullRecipeResponse.builder()
            .id(1L)
            .name("recipe 1")
            .ingredients(List.of(RecipeIngredientResponse.builder()
                    .ingredient(Ingredient.builder().name("ing 1").build())
                    .quantity(("1 gram")).build()))
            .build();


    @Test
    @DisplayName("GET /recipe/{id}}")
    void testGetRecipe() throws Exception {
        doReturn(recipeWithOneIngredient).when(service).getRecipe(1L);

        mockMvc.perform(get("/recipe/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value("recipe 1"))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.ingredients", hasSize(1)));
    }

    @Test
    @DisplayName("GET /recipes")
    void testGetRecipes() throws Exception {
        doReturn(List.of(recipeWithOneIngredient)).when(service).getRecipes();

        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("recipe 1"))
                .andExpect(jsonPath("$[0].ingredients", hasSize(1)));
    }

    @Test
    @DisplayName("POST /recipe")
    void testAddRecipe() throws Exception {
        RecipeRequest recipeRequest = RecipeRequest.builder().name("recipe 1").build();
        doReturn(recipeWithOneIngredient).when(service).addRecipe(recipeRequest);

        mockMvc.perform(
                post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("recipe 1"))
                .andExpect(jsonPath("$.ingredients", hasSize(1)))
                .andExpect(jsonPath("$.ingredients[0].ingredient.name").value("ing 1"))
                .andExpect(jsonPath("$.ingredients[0].quantity").value("1 gram"));
    }

    @Test
    @DisplayName("PUT /recipe/{id}")
    void updateRecipe() throws Exception {
        RecipeRequest recipeRequest = RecipeRequest.builder().name("recipe 1").build();
        doReturn(recipeWithOneIngredient).when(service).updateRecipe(1L, recipeRequest);

        mockMvc.perform(
                put("/recipe/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("recipe 1"))
                .andExpect(jsonPath("$.ingredients", hasSize(1)))
                .andExpect(jsonPath("$.ingredients[0].ingredient.name").value("ing 1"))
                .andExpect(jsonPath("$.ingredients[0].quantity").value("1 gram"));
    }

    @Test
    @DisplayName("DELETE /recipe/{id}")
    void deleteRecipe() throws Exception {
        doNothing().when(service).deleteRecipe(1L);

        mockMvc.perform(delete("/recipe/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /recipe/{id}/addIngredient")
    void addIngredient() throws Exception {
        IngredientWithQuantity ingredientWithQuantity = IngredientWithQuantity.builder()
                .name("ing 1")
                .quantity("1 gram")
                .build();

        doReturn(recipeWithOneIngredient).when(service).addIngredientToRecipe(1L, ingredientWithQuantity);

        mockMvc.perform(
                post("/recipe/{id}/addIngredient", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ingredientWithQuantity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("recipe 1"))
                .andExpect(jsonPath("$.ingredients", hasSize(1)))
                .andExpect(jsonPath("$.ingredients[0].ingredient.name").value("ing 1"))
                .andExpect(jsonPath("$.ingredients[0].quantity").value("1 gram"));
    }

    @Test
    @DisplayName("DELETE /recipe/{id}/removeIngredient/{ingredientId}")
    void removeIngredient() throws Exception {
        final FullRecipeResponse recipeWithoutIngredients = FullRecipeResponse.builder()
                .id(1L)
                .name("recipe 1")
                .ingredients(emptyList())
                .build();

        doReturn(recipeWithoutIngredients).when(service).removeIngredient(1L, 1L);

        mockMvc.perform(
                delete("/recipe/{id}/removeIngredient/{ingredientId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeWithOneIngredient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("recipe 1"))
                .andExpect(jsonPath("$.ingredients", hasSize(0)));
    }
}
