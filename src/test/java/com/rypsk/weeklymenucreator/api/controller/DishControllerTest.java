package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DishResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Recipe;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import com.rypsk.weeklymenucreator.api.service.DishService;
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

@WebMvcTest(controllers = DishController.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@Import(DishControllerTest.TestConfig.class)
class DishControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public DishService dishService() {
            return mock(DishService.class);
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
    private DishService dishService;

    private DishRequest validDishRequest;
    private DishResponse dishResponse;
    private List<DishResponse> dishResponses;

    @BeforeEach
    void setUp() {
        // Setup test data
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");

        validDishRequest = new DishRequest(
                "Test Dish",
                "Test Description",
                recipe,
                FoodType.MEAT,
                1L
        );

        dishResponse = new DishResponse(
                1L,
                "Test Dish",
                "Test Description",
                recipe,
                FoodType.MEAT,
                1L
        );

        dishResponses = Collections.singletonList(dishResponse);
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDish_ReturnsDish() throws Exception {
        when(dishService.getDish(1L)).thenReturn(dishResponse);

        mockMvc.perform(get("/api/v1/dishes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Dish")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.recipe.id", is(1)))
                .andExpect(jsonPath("$.recipe.name", is("Test Recipe")))
                .andExpect(jsonPath("$.foodType", is("MEAT")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateDish_ReturnsUpdatedDish() throws Exception {
        when(dishService.updateDish(eq(1L), any(DishRequest.class))).thenReturn(dishResponse);

        mockMvc.perform(put("/api/v1/dishes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDishRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Dish")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void deleteDish_ReturnsNoContent() throws Exception {
        doNothing().when(dishService).deleteDish(1L);

        mockMvc.perform(delete("/api/v1/dishes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    void createDishForMe_ReturnsCreatedDish() throws Exception {
        when(dishService.createDishForMe(any(DishRequest.class))).thenReturn(dishResponse);

        mockMvc.perform(post("/api/v1/dishes/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDishRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Dish")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createDishForUser_ReturnsCreatedDish() throws Exception {
        when(dishService.createDishForUser(any(DishRequest.class), eq(1L))).thenReturn(dishResponse);

        mockMvc.perform(post("/api/v1/dishes/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDishRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Dish")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDishesForMe_ReturnsListOfDishes() throws Exception {
        when(dishService.getDishesForMe()).thenReturn(dishResponses);

        mockMvc.perform(get("/api/v1/dishes/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Dish")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDishesForUser_ReturnsListOfDishes() throws Exception {
        when(dishService.getDishesForUser(1L)).thenReturn(dishResponses);

        mockMvc.perform(get("/api/v1/dishes/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Dish")));
    }
}