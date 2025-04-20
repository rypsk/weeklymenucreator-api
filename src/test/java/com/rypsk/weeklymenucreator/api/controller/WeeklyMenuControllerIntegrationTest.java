package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;
import com.rypsk.weeklymenucreator.api.model.enumeration.Role;
import com.rypsk.weeklymenucreator.api.repository.DailyMenuRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.repository.WeeklyMenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class WeeklyMenuControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeeklyMenuRepository weeklyMenuRepository;

    @Autowired
    private DailyMenuRepository dailyMenuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private DailyMenu testDailyMenu;
    private WeeklyMenuRequest validWeeklyMenuRequest;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules(); // For LocalDate serialization

        // Clean up the database before each test
        weeklyMenuRepository.deleteAll();
        dailyMenuRepository.deleteAll();
        userRepository.deleteAll();

        // Create a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole(Role.ROLE_USER);
        testUser.setEnabled(true);
        testUser.setAccountNonExpired(true);
        testUser.setAccountNonLocked(true);
        testUser.setCredentialsNonExpired(true);
        userRepository.save(testUser);

        // Create a test daily menu
        testDailyMenu = new DailyMenu();
        testDailyMenu.setDayOfWeek(DayOfWeek.MONDAY);
        testDailyMenu.setDate(LocalDate.now());
        testDailyMenu.setDishes(new ArrayList<>());
        testDailyMenu.setUser(testUser);
        dailyMenuRepository.save(testDailyMenu);

        // Setup test data
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);

        List<Long> dailyMenuIds = Collections.singletonList(testDailyMenu.getId());

        validWeeklyMenuRequest = new WeeklyMenuRequest(
                startDate,
                endDate,
                dailyMenuIds
        );
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void createWeeklyMenuForMe_ThenGetWeeklyMenu() throws Exception {
        // Create a weekly menu
        String responseJson = mockMvc.perform(post("/api/v1/weekly-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validWeeklyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.dailyMenus", hasSize(1)))
                .andExpect(jsonPath("$.dailyMenus[0].id", is(testDailyMenu.getId().intValue())))
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer weeklyMenuId = JsonPath.parse(responseJson).read("$.id");

        // Get the weekly menu by ID
        mockMvc.perform(get("/api/v1/weekly-menus/" + weeklyMenuId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(weeklyMenuId)))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getWeeklyMenusForMe_ReturnsEmptyList_WhenNoWeeklyMenus() throws Exception {
        mockMvc.perform(get("/api/v1/weekly-menus/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void createAndUpdateWeeklyMenu() throws Exception {
        // Create a weekly menu
        String responseJson = mockMvc.perform(post("/api/v1/weekly-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validWeeklyMenuRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer weeklyMenuId = JsonPath.parse(responseJson).read("$.id");

        // Update the weekly menu with a new end date
        LocalDate newEndDate = LocalDate.now().plusDays(14);
        WeeklyMenuRequest updatedRequest = new WeeklyMenuRequest(
                validWeeklyMenuRequest.startDate(),
                newEndDate,
                validWeeklyMenuRequest.dailyMenuIds()
        );

        mockMvc.perform(put("/api/v1/weekly-menus/" + weeklyMenuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(weeklyMenuId)))
                .andExpect(jsonPath("$.endDate", is(newEndDate.toString())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void createAndDeleteWeeklyMenu() throws Exception {
        // Create a weekly menu
        String responseJson = mockMvc.perform(post("/api/v1/weekly-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validWeeklyMenuRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer weeklyMenuId = JsonPath.parse(responseJson).read("$.id");

        // Delete the weekly menu
        mockMvc.perform(delete("/api/v1/weekly-menus/" + weeklyMenuId))
                .andExpect(status().isOk());

        // Verify the weekly menu is deleted
        mockMvc.perform(get("/api/v1/weekly-menus/" + weeklyMenuId))
                .andExpect(status().is5xxServerError()); // Assuming 500 is returned when weekly menu not found
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void createWeeklyMenuForUser() throws Exception {
        mockMvc.perform(post("/api/v1/weekly-menus/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validWeeklyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.dailyMenus", hasSize(1)))
                .andExpect(jsonPath("$.dailyMenus[0].id", is(testDailyMenu.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getWeeklyMenusForUser() throws Exception {
        // Create a weekly menu first
        mockMvc.perform(post("/api/v1/weekly-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validWeeklyMenuRequest)))
                .andExpect(status().isOk());

        // Get weekly menus for user
        mockMvc.perform(get("/api/v1/weekly-menus/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(testUser.getId().intValue())));
    }
}