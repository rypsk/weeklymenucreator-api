package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.entity.Recipe;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.Difficulty;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import com.rypsk.weeklymenucreator.api.repository.DishRepository;
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

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DishControllerH2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Recipe testRecipe;
    private DishRequest validDishRequest;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        dishRepository.deleteAll();
        recipeRepository.deleteAll();
        userRepository.deleteAll();

        // Create a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        // Create a test recipe
        testRecipe = new Recipe();
        testRecipe.setName("Test Recipe");
        testRecipe.setDescription("Test Recipe Description");
        testRecipe.setDifficulty(Difficulty.EASY);
        testRecipe.setIngredients(new ArrayList<>());
        testRecipe.setUser(testUser);
        recipeRepository.save(testRecipe);

        // Setup test data
        validDishRequest = new DishRequest(
                "Test Dish",
                "Test Description",
                testRecipe,
                FoodType.MEAT,
                testUser.getId()
        );
    }

    @Test
    @WithMockUser(username = "testuser")
    void createDishForMe_ThenGetDish() throws Exception {
        // Create a dish
        String responseJson = mockMvc.perform(post("/api/v1/dishes/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDishRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Dish")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.recipe.id", is(testRecipe.getId().intValue())))
                .andExpect(jsonPath("$.foodType", is("MEAT")))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer dishId = JsonPath.parse(responseJson).read("$.id");

        // Get the dish by ID
        mockMvc.perform(get("/api/v1/dishes/" + dishId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(dishId)))
                .andExpect(jsonPath("$.name", is("Test Dish")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDishesForMe_ReturnsEmptyList_WhenNoDishes() throws Exception {
        mockMvc.perform(get("/api/v1/dishes/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAndUpdateDish() throws Exception {
        // Create a dish
        String responseJson = mockMvc.perform(post("/api/v1/dishes/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDishRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer dishId = JsonPath.parse(responseJson).read("$.id");

        // Update the dish
        DishRequest updatedRequest = new DishRequest(
                "Updated Dish",
                "Updated Description",
                testRecipe,
                FoodType.VEGETABLES,
                testUser.getId()
        );

        mockMvc.perform(put("/api/v1/dishes/" + dishId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(dishId)))
                .andExpect(jsonPath("$.name", is("Updated Dish")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.foodType", is("VEGETABLES")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAndDeleteDish() throws Exception {
        // Create a dish
        String responseJson = mockMvc.perform(post("/api/v1/dishes/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDishRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer dishId = JsonPath.parse(responseJson).read("$.id");

        // Delete the dish
        mockMvc.perform(delete("/api/v1/dishes/" + dishId))
                .andExpect(status().isNoContent());

        // Verify the dish is deleted
        mockMvc.perform(get("/api/v1/dishes/" + dishId))
                .andExpect(status().is5xxServerError()); // Assuming 500 is returned when dish not found
    }
}
