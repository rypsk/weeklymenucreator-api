package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientRequest;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientResponse;
import com.rypsk.weeklymenucreator.api.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    private ObjectMapper objectMapper;
    private IngredientRequest validIngredientRequest;
    private IngredientResponse ingredientResponse;
    private List<IngredientResponse> ingredientResponses;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        validIngredientRequest = new IngredientRequest(
                "Test Ingredient",
                "100",
                "g"
        );

        ingredientResponse = new IngredientResponse(
                1L,
                "Test Ingredient",
                "100g",
                1L
        );

        ingredientResponses = Collections.singletonList(ingredientResponse);
    }

    @Test
    void getIngredient_ReturnsIngredient() throws Exception {
        when(ingredientService.getIngredient(1L)).thenReturn(ingredientResponse);

        mockMvc.perform(get("/api/v1/ingredients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Ingredient")))
                .andExpect(jsonPath("$.quantity", is("100g")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void updateIngredient_ReturnsUpdatedIngredient() throws Exception {
        when(ingredientService.updateIngredient(eq(1L), any(IngredientRequest.class))).thenReturn(ingredientResponse);

        mockMvc.perform(put("/api/v1/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validIngredientRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Ingredient")));
    }

    @Test
    void deleteIngredient_ReturnsNoContent() throws Exception {
        doNothing().when(ingredientService).deleteIngredient(1L);

        mockMvc.perform(delete("/api/v1/ingredients/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    void createIngredientForMe_ReturnsCreatedIngredient() throws Exception {
        when(ingredientService.createIngredientForMe(any(IngredientRequest.class))).thenReturn(ingredientResponse);

        mockMvc.perform(post("/api/v1/ingredients/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validIngredientRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Ingredient")));
    }

    @Test
    void createIngredientForUser_ReturnsCreatedIngredient() throws Exception {
        when(ingredientService.createIngredientForUser(any(IngredientRequest.class), eq(1L))).thenReturn(ingredientResponse);

        mockMvc.perform(post("/api/v1/ingredients/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validIngredientRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Ingredient")));
    }

    @Test
    void getIngredientsForMe_ReturnsListOfIngredients() throws Exception {
        when(ingredientService.getIngredientsForMe()).thenReturn(ingredientResponses);

        mockMvc.perform(get("/api/v1/ingredients/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Ingredient")));
    }

    @Test
    void getIngredientsForUser_ReturnsListOfIngredients() throws Exception {
        when(ingredientService.getIngredientsForUser(1L)).thenReturn(ingredientResponses);

        mockMvc.perform(get("/api/v1/ingredients/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Ingredient")));
    }

}
