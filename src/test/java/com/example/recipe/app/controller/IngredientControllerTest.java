package com.example.recipe.app.controller;

import com.example.recipe.app.exeption.NotFoundException;
import com.example.recipe.app.model.entity.Ingredient;
import com.example.recipe.app.model.request.IngredientRequest;
import com.example.recipe.app.service.IngredientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredientController.class)
public class IngredientControllerTest {

    @MockBean
    private IngredientService service;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final Ingredient ing = Ingredient.builder().name("ing 1").build();


    @Test
    @DisplayName("NotFound GET /ingredient/1")
    void testGetIngredientNotFound() throws Exception {
        doThrow(new NotFoundException("No ingredient with id 1 found")).when(service).getIngredient(1L);

        mockMvc.perform(get("/ingredient/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("No ingredient with id 1 found"))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("InvalidArgument GET /ingredient/arg")
    void testGetIngredientInvalidArgument() throws Exception {
        mockMvc.perform(get("/ingredient/{id}", "arg"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Invalid argument"));
    }

    @Test
    @DisplayName("GET /ingredients")
    void testGetIngredients() throws Exception {
        doReturn(List.of(ing)).when(service).getIngredients();

        mockMvc.perform(get("/ingredient"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("ing 1"));
    }

    @Test
    @DisplayName("POST /ingredient")
    void testAddIngredient() throws Exception {
        doReturn(ing).when(service).addIngredient("ing 1");
        IngredientRequest ingredientRequest = IngredientRequest.builder().name("ing 1").build();

        mockMvc.perform(
                        post("/ingredient").contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(ingredientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("ing 1"));
    }

    @Test
    @DisplayName("InvalidBody /ingredient")
    void testAddIngredientInvalidBody() throws Exception {
        IngredientRequest ingredientRequest = IngredientRequest.builder().name("i").build();

        mockMvc.perform(
                        post("/ingredient")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(ingredientRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name must be at least 3 characters long"));
    }

    @Test
    @DisplayName("Integrity exception")
    void dataIntegrityExceptions() throws Exception {
        doThrow(new DataIntegrityViolationException("Invalid argument")).when(service).getIngredient(1L);

        mockMvc.perform(get("/ingredient/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Invalid argument"))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}
