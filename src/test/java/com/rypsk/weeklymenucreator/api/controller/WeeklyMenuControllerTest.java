package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.AutoGenerateWeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;
import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import com.rypsk.weeklymenucreator.api.service.EmailService;
import com.rypsk.weeklymenucreator.api.service.ExportService;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WeeklyMenuController.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@Import(WeeklyMenuControllerTest.TestConfig.class)
class WeeklyMenuControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public WeeklyMenuService weeklyMenuService() {
            return mock(WeeklyMenuService.class);
        }

        @Bean
        public ExportService exportService() {
            return mock(ExportService.class);
        }

        @Bean
        public EmailService emailService() {
            return mock(EmailService.class);
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
    private WeeklyMenuService weeklyMenuService;

    @Autowired
    private ExportService exportService;

    @Autowired
    private EmailService emailService;

    private WeeklyMenuRequest validWeeklyMenuRequest;
    private WeeklyMenuResponse weeklyMenuResponse;
    private List<WeeklyMenuResponse> weeklyMenuResponses;
    private AutoGenerateWeeklyMenuRequest validAutoGenerateRequest;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules(); // For LocalDate serialization

        // Setup test data
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);

        List<Long> dailyMenuIds = Collections.singletonList(1L);

        validWeeklyMenuRequest = new WeeklyMenuRequest(
                startDate,
                endDate,
                dailyMenuIds
        );

        DailyMenu dailyMenu = new DailyMenu();
        dailyMenu.setId(1L);
        dailyMenu.setDayOfWeek(DayOfWeek.MONDAY);

        List<DailyMenu> dailyMenus = Collections.singletonList(dailyMenu);

        weeklyMenuResponse = new WeeklyMenuResponse(
                1L,
                1L,
                startDate,
                endDate,
                dailyMenus
        );

        weeklyMenuResponses = Collections.singletonList(weeklyMenuResponse);

        Set<DishType> dishTypes = new HashSet<>();
        dishTypes.add(DishType.LUNCH);
        dishTypes.add(DishType.DINNER);

        Map<FoodType, Integer> preferences = new HashMap<>();
        preferences.put(FoodType.MEAT, 3);
        preferences.put(FoodType.VEGETABLES, 2);

        validAutoGenerateRequest = new AutoGenerateWeeklyMenuRequest(
                startDate,
                endDate,
                dishTypes,
                preferences,
                DietType.BALANCED,
                false
        );
    }

    @Test
    @WithMockUser(username = "testuser")
    void getWeeklyMenu_ReturnsWeeklyMenu() throws Exception {
        when(weeklyMenuService.getWeeklyMenu(1L)).thenReturn(weeklyMenuResponse);

        mockMvc.perform(get("/api/v1/weekly-menus/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.dailyMenus", hasSize(1)))
                .andExpect(jsonPath("$.dailyMenus[0].id", is(1)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateWeeklyMenu_ReturnsUpdatedWeeklyMenu() throws Exception {
        when(weeklyMenuService.updateWeeklyMenu(eq(1L), any(WeeklyMenuRequest.class))).thenReturn(weeklyMenuResponse);

        mockMvc.perform(put("/api/v1/weekly-menus/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validWeeklyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void deleteWeeklyMenu_ReturnsOk() throws Exception {
        doNothing().when(weeklyMenuService).deleteWeeklyMenu(1L);

        mockMvc.perform(delete("/api/v1/weekly-menus/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void exportWeeklyMenu_ReturnsExportedData() throws Exception {
        byte[] pdfData = "PDF content".getBytes();
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=weekly-menu-1.pdf")
                .body(pdfData);

        when(exportService.exportWeeklyMenu(1L, "pdf")).thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/weekly-menus/1/export/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=weekly-menu-1.pdf"))
                .andExpect(content().bytes(pdfData));
    }

    @Test
    @WithMockUser(username = "testuser")
    void sendWeeklyMenuByEmail_ReturnsOk() throws Exception {
        doNothing().when(emailService).sendWeeklyMenuByEmail(eq(1L), isNull());

        mockMvc.perform(post("/api/v1/weekly-menus/1/email"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void autoGenerateWeeklyMenu_ReturnsGeneratedWeeklyMenu() throws Exception {
        when(weeklyMenuService.autoGenerateWeeklyMenuForMe(any(AutoGenerateWeeklyMenuRequest.class)))
                .thenReturn(weeklyMenuResponse);

        mockMvc.perform(post("/api/v1/weekly-menus/users/me/auto-generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAutoGenerateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createWeeklyMenuForMe_ReturnsCreatedWeeklyMenu() throws Exception {
        when(weeklyMenuService.createWeeklyMenuForMe(any(WeeklyMenuRequest.class))).thenReturn(weeklyMenuResponse);

        mockMvc.perform(post("/api/v1/weekly-menus/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validWeeklyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createWeeklyMenuForUser_ReturnsCreatedWeeklyMenu() throws Exception {
        when(weeklyMenuService.createWeeklyMenuForUser(any(WeeklyMenuRequest.class), eq(1L))).thenReturn(weeklyMenuResponse);

        mockMvc.perform(post("/api/v1/weekly-menus/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validWeeklyMenuRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getWeeklyMenusForMe_ReturnsListOfWeeklyMenus() throws Exception {
        when(weeklyMenuService.getWeeklyMenusForMe()).thenReturn(weeklyMenuResponses);

        mockMvc.perform(get("/api/v1/weekly-menus/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getWeeklyMenusForUser_ReturnsListOfWeeklyMenus() throws Exception {
        when(weeklyMenuService.getWeeklyMenusForUser(1L)).thenReturn(weeklyMenuResponses);

        mockMvc.perform(get("/api/v1/weekly-menus/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)));
    }
}