package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import com.rypsk.weeklymenucreator.api.repository.DailyMenuRepository;
import com.rypsk.weeklymenucreator.api.repository.DishRepository;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DailyMenuControllerH2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DailyMenuRepository dailyMenuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Dish testDish;
    private DailyMenuRequest validDailyMenuRequest;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        dailyMenuRepository.deleteAll();
        dishRepository.deleteAll();
        userRepository.deleteAll();

        // Create a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        // Create a test dish
        testDish = new Dish();
        testDish.setName("Test Dish");
        testDish.setDescription("Test Description");
        testDish.setDishType(DishType.LUNCH);
        testDish.setFoodType(FoodType.MEAT);
        testDish.setDietType(DietType.BALANCED);
        testDish.setUser(testUser);
        dishRepository.save(testDish);

        // Setup test data
        List<Long> dishIds = new ArrayList<>();
        dishIds.add(testDish.getId());

        validDailyMenuRequest = new DailyMenuRequest(
                testUser.getId(),
                DayOfWeek.MONDAY,
                dishIds
        );
    }

    @Test
    @WithMockUser(username = "testuser")
    void createDailyMenuForMe_ThenGetDailyMenu() throws Exception {
        // Create a daily menu
        String responseJson = mockMvc.perform(post("/api/v1/daily-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDailyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")))
                .andExpect(jsonPath("$.dishes", hasSize(1)))
                .andExpect(jsonPath("$.dishes[0].id", is(testDish.getId().intValue())))
                .andExpect(jsonPath("$.dishes[0].name", is("Test Dish")))
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer dailyMenuId = JsonPath.parse(responseJson).read("$.id");

        // Get the daily menu by ID
        mockMvc.perform(get("/api/v1/daily-menus/" + dailyMenuId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(dailyMenuId)))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDailyMenusForMe_ReturnsEmptyList_WhenNoDailyMenus() throws Exception {
        mockMvc.perform(get("/api/v1/daily-menus/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAndUpdateDailyMenu() throws Exception {
        // Create a daily menu
        String responseJson = mockMvc.perform(post("/api/v1/daily-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDailyMenuRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer dailyMenuId = JsonPath.parse(responseJson).read("$.id");

        // Update the daily menu
        DailyMenuRequest updatedRequest = new DailyMenuRequest(
                testUser.getId(),
                DayOfWeek.TUESDAY,
                validDailyMenuRequest.dishIds()
        );

        mockMvc.perform(put("/api/v1/daily-menus/" + dailyMenuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(dailyMenuId)))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.dayOfWeek", is("TUESDAY")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAndDeleteDailyMenu() throws Exception {
        // Create a daily menu
        String responseJson = mockMvc.perform(post("/api/v1/daily-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDailyMenuRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer dailyMenuId = JsonPath.parse(responseJson).read("$.id");

        // Delete the daily menu
        mockMvc.perform(delete("/api/v1/daily-menus/" + dailyMenuId))
                .andExpect(status().isNoContent());

        // Verify the daily menu is deleted
        mockMvc.perform(get("/api/v1/daily-menus/" + dailyMenuId))
                .andExpect(status().is5xxServerError()); // Assuming 500 is returned when daily menu not found
    }
}