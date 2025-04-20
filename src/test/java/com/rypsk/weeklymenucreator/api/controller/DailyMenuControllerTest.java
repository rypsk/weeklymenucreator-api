package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;
import com.rypsk.weeklymenucreator.api.service.DailyMenuService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DailyMenuController.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@Import(DailyMenuControllerTest.TestConfig.class)
class DailyMenuControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public DailyMenuService dailyMenuService() {
            return mock(DailyMenuService.class);
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
    private DailyMenuService dailyMenuService;

    private DailyMenuRequest validDailyMenuRequest;
    private DailyMenuResponse dailyMenuResponse;
    private List<DailyMenuResponse> dailyMenuResponses;

    @BeforeEach
    void setUp() {
        // Setup test data
        List<Long> dishIds = Collections.singletonList(1L);
        List<Dish> dishes = new ArrayList<>();
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Test Dish");
        dish.setDescription("Test Description");
        dishes.add(dish);

        validDailyMenuRequest = new DailyMenuRequest(
                1L,
                DayOfWeek.MONDAY,
                dishIds
        );

        dailyMenuResponse = new DailyMenuResponse(
                1L,
                1L,
                DayOfWeek.MONDAY,
                dishes
        );

        dailyMenuResponses = Collections.singletonList(dailyMenuResponse);
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDailyMenu_ReturnsDailyMenu() throws Exception {
        when(dailyMenuService.getDailyMenu(1L)).thenReturn(dailyMenuResponse);

        mockMvc.perform(get("/api/v1/daily-menus/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")))
                .andExpect(jsonPath("$.dishes", hasSize(1)))
                .andExpect(jsonPath("$.dishes[0].id", is(1)))
                .andExpect(jsonPath("$.dishes[0].name", is("Test Dish")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateDailyMenu_ReturnsUpdatedDailyMenu() throws Exception {
        when(dailyMenuService.updateDailyMenu(eq(1L), any(DailyMenuRequest.class))).thenReturn(dailyMenuResponse);

        mockMvc.perform(put("/api/v1/daily-menus/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDailyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void deleteDailyMenu_ReturnsNoContent() throws Exception {
        doNothing().when(dailyMenuService).deleteDailyMenu(1L);

        mockMvc.perform(delete("/api/v1/daily-menus/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    void createDailyMenuForMe_ReturnsCreatedDailyMenu() throws Exception {
        when(dailyMenuService.createDailyMenuForMe(any(DailyMenuRequest.class))).thenReturn(dailyMenuResponse);

        mockMvc.perform(post("/api/v1/daily-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDailyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createDailyMenuForUser_ReturnsCreatedDailyMenu() throws Exception {
        when(dailyMenuService.createDailyMenuForUser(any(DailyMenuRequest.class), eq(1L))).thenReturn(dailyMenuResponse);

        mockMvc.perform(post("/api/v1/daily-menus/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDailyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDailyMenusForMe_ReturnsListOfDailyMenus() throws Exception {
        when(dailyMenuService.getDailyMenusForMe()).thenReturn(dailyMenuResponses);

        mockMvc.perform(get("/api/v1/daily-menus/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].dayOfWeek", is("MONDAY")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDailyMenusForUser_ReturnsListOfDailyMenus() throws Exception {
        when(dailyMenuService.getDailyMenusForUser(1L)).thenReturn(dailyMenuResponses);

        mockMvc.perform(get("/api/v1/daily-menus/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].dayOfWeek", is("MONDAY")));
    }
}