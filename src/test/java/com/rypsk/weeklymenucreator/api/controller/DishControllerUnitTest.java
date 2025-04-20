package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DishResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Recipe;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import com.rypsk.weeklymenucreator.api.service.DishService;
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
class DishControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private DishService dishService;

    @InjectMocks
    private DishController dishController;

    private ObjectMapper objectMapper;
    private DishRequest validDishRequest;
    private DishResponse dishResponse;
    private List<DishResponse> dishResponses;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dishController).build();
        objectMapper = new ObjectMapper();

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
    void deleteDish_ReturnsNoContent() throws Exception {
        doNothing().when(dishService).deleteDish(1L);

        mockMvc.perform(delete("/api/v1/dishes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
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