package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientRequest;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientResponse;
import com.rypsk.weeklymenucreator.api.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = IngredientController.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@Import(IngredientControllerTest.TestConfig.class)
class IngredientControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public IngredientService ingredientService() {
            return mock(IngredientService.class);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IngredientService ingredientService;

    private IngredientRequest validIngredientRequest;
    private IngredientResponse ingredientResponse;
    private List<IngredientResponse> ingredientResponses;

    @BeforeEach
    void setUp() {
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
    @WithMockUser(username = "testuser")
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
    @WithMockUser(username = "testuser")
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
    @WithMockUser(username = "testuser")
    void deleteIngredient_ReturnsNoContent() throws Exception {
        doNothing().when(ingredientService).deleteIngredient(1L);

        mockMvc.perform(delete("/api/v1/ingredients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
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
    @WithMockUser(username = "testuser")
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
    @WithMockUser(username = "testuser")
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
    @WithMockUser(username = "testuser")
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