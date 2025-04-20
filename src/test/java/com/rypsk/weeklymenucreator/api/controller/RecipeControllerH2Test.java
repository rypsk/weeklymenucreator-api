package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.rypsk.weeklymenucreator.api.model.dto.RecipeRequest;
import com.rypsk.weeklymenucreator.api.model.entity.Ingredient;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.Difficulty;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.Season;
import com.rypsk.weeklymenucreator.api.repository.RecipeRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecipeControllerH2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private RecipeRequest validRecipeRequest;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        recipeRepository.deleteAll();
        userRepository.deleteAll();

        // Create a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        // Setup test data
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Test Ingredient");
        ingredient.setQuantity("100g");
        ingredient.setUser(testUser);

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
    }

    @Test
    @WithMockUser(username = "testuser")
    void createRecipeForMe_ThenGetRecipe() throws Exception {
        // Create a recipe
        String responseJson = mockMvc.perform(post("/api/v1/recipes/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRecipeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Recipe")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.ingredients", hasSize(1)))
                .andExpect(jsonPath("$.difficulty", is("EASY")))
                .andExpect(jsonPath("$.season", hasSize(1)))
                .andExpect(jsonPath("$.public", is(false)))
                .andExpect(jsonPath("$.createdBy", is("testuser")))
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer recipeId = JsonPath.parse(responseJson).read("$.id");

        // Get the recipe by ID
        mockMvc.perform(get("/api/v1/recipes/" + recipeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(recipeId)))
                .andExpect(jsonPath("$.name", is("Test Recipe")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getRecipesForMe_ReturnsEmptyList_WhenNoRecipes() throws Exception {
        mockMvc.perform(get("/api/v1/recipes/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAndUpdateRecipe() throws Exception {
        // Create a recipe
        String responseJson = mockMvc.perform(post("/api/v1/recipes/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRecipeRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer recipeId = JsonPath.parse(responseJson).read("$.id");

        // Update the recipe
        RecipeRequest updatedRequest = new RecipeRequest(
                "Updated Recipe",
                "Updated Description",
                validRecipeRequest.ingredients(),
                validRecipeRequest.difficulty(),
                validRecipeRequest.season(),
                validRecipeRequest.dishType(),
                validRecipeRequest.dietType(),
                true
        );

        mockMvc.perform(put("/api/v1/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(recipeId)))
                .andExpect(jsonPath("$.name", is("Updated Recipe")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.public", is(true)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAndDeleteRecipe() throws Exception {
        // Create a recipe
        String responseJson = mockMvc.perform(post("/api/v1/recipes/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRecipeRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer recipeId = JsonPath.parse(responseJson).read("$.id");

        // Delete the recipe
        mockMvc.perform(delete("/api/v1/recipes/" + recipeId))
                .andExpect(status().isNoContent());

        // Verify the recipe is deleted
        mockMvc.perform(get("/api/v1/recipes/" + recipeId))
                .andExpect(status().is5xxServerError()); // Assuming 500 is returned when recipe not found
    }
}