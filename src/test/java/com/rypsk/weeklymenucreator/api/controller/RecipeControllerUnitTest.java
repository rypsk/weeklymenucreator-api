package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.RecipeRequest;
import com.rypsk.weeklymenucreator.api.model.dto.RecipeResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Ingredient;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.Difficulty;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.Season;
import com.rypsk.weeklymenucreator.api.service.RecipeService;
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
class RecipeControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    private ObjectMapper objectMapper;
    private RecipeRequest validRecipeRequest;
    private RecipeResponse recipeResponse;
    private List<RecipeResponse> recipeResponses;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Test Ingredient");
        ingredient.setQuantity("100g");

        validRecipeRequest = new RecipeRequest(
                "Test Recipe",
                "Test Description",
                Collections.singletonList(ingredient),
                Difficulty.EASY,
                Collections.singletonList(Season.SPRING),
                DishType.LUNCH,
                DietType.BALANCED,
                false
        );

        recipeResponse = new RecipeResponse(
                1L,
                "Test Recipe",
                "Test Description",
                Collections.singletonList(ingredient),
                Difficulty.EASY,
                Collections.singletonList(Season.SPRING),
                false,
                "testuser"
        );

        recipeResponses = Collections.singletonList(recipeResponse);
    }

    @Test
    void getRecipe_ReturnsRecipe() throws Exception {
        when(recipeService.getRecipe(1L)).thenReturn(recipeResponse);

        mockMvc.perform(get("/api/v1/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Recipe")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.ingredients", hasSize(1)))
                .andExpect(jsonPath("$.difficulty", is("EASY")))
                .andExpect(jsonPath("$.season", hasSize(1)))
                .andExpect(jsonPath("$.isPublic", is(false)))
                .andExpect(jsonPath("$.createdBy", is("testuser")));
    }

    @Test
    void updateRecipe_ReturnsUpdatedRecipe() throws Exception {
        when(recipeService.updateRecipe(eq(1L), any(RecipeRequest.class))).thenReturn(recipeResponse);

        mockMvc.perform(put("/api/v1/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRecipeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Recipe")));
    }

    @Test
    void deleteRecipe_ReturnsNoContent() throws Exception {
        doNothing().when(recipeService).deleteRecipe(1L);

        mockMvc.perform(delete("/api/v1/recipes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPublicRecipes_ReturnsListOfRecipes() throws Exception {
        when(recipeService.getPublicRecipes()).thenReturn(recipeResponses);

        mockMvc.perform(get("/api/v1/recipes/public"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Recipe")));
    }

    @Test
    void createRecipeForMe_ReturnsCreatedRecipe() throws Exception {
        when(recipeService.createRecipeForMe(any(RecipeRequest.class))).thenReturn(recipeResponse);

        mockMvc.perform(post("/api/v1/recipes/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRecipeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Recipe")));
    }

    @Test
    void createRecipeForUser_ReturnsCreatedRecipe() throws Exception {
        when(recipeService.createRecipeForUser(any(RecipeRequest.class), eq(1L))).thenReturn(recipeResponse);

        mockMvc.perform(post("/api/v1/recipes/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRecipeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Recipe")));
    }

    @Test
    void getRecipesForMe_ReturnsListOfRecipes() throws Exception {
        when(recipeService.getRecipesForMe()).thenReturn(recipeResponses);

        mockMvc.perform(get("/api/v1/recipes/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Recipe")));
    }

    @Test
    void getRecipesForUser_ReturnsListOfRecipes() throws Exception {
        when(recipeService.getRecipesForUser(1L)).thenReturn(recipeResponses);

        mockMvc.perform(get("/api/v1/recipes/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Recipe")));
    }

    @Test
    void getAvailableRecipesForMe_ReturnsListOfRecipes() throws Exception {
        when(recipeService.getAvailableRecipesForMe()).thenReturn(recipeResponses);

        mockMvc.perform(get("/api/v1/recipes/users/me/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Recipe")));
    }

    @Test
    void getAvailableRecipesForUser_ReturnsListOfRecipes() throws Exception {
        when(recipeService.getAvailableRecipesForUser(1L)).thenReturn(recipeResponses);

        mockMvc.perform(get("/api/v1/recipes/users/1/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Recipe")));
    }
}
