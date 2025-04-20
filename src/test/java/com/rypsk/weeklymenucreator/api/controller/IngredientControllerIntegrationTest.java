package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientRequest;
import com.rypsk.weeklymenucreator.api.model.entity.Ingredient;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.Role;
import com.rypsk.weeklymenucreator.api.repository.IngredientRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class IngredientControllerIntegrationTest {

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
    private IngredientRepository ingredientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Ingredient testIngredient;
    private IngredientRequest validIngredientRequest;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        ingredientRepository.deleteAll();
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

        // Create a test ingredient
        testIngredient = new Ingredient();
        testIngredient.setName("Test Ingredient");
        testIngredient.setQuantity("100g");
        testIngredient.setUser(testUser);
        ingredientRepository.save(testIngredient);

        // Setup test data
        validIngredientRequest = new IngredientRequest(
                "New Ingredient",
                "200",
                "ml"
        );
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getIngredient_ReturnsIngredient() throws Exception {
        mockMvc.perform(get("/api/v1/ingredients/" + testIngredient.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testIngredient.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Ingredient")))
                .andExpect(jsonPath("$.quantity", is("100g")))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void updateIngredient_ReturnsUpdatedIngredient() throws Exception {
        IngredientRequest updateRequest = new IngredientRequest(
                "Updated Ingredient",
                "300",
                "g"
        );

        mockMvc.perform(put("/api/v1/ingredients/" + testIngredient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testIngredient.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Updated Ingredient")))
                .andExpect(jsonPath("$.quantity", is("300g")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void deleteIngredient_DeletesIngredient() throws Exception {
        // Delete the ingredient
        mockMvc.perform(delete("/api/v1/ingredients/" + testIngredient.getId()))
                .andExpect(status().isNoContent());

        // Verify the ingredient was deleted
        mockMvc.perform(get("/api/v1/ingredients/" + testIngredient.getId()))
                .andExpect(status().is5xxServerError()); // Assuming 500 is returned when ingredient not found
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void createIngredientForMe_ReturnsCreatedIngredient() throws Exception {
        String responseJson = mockMvc.perform(post("/api/v1/ingredients/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validIngredientRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("New Ingredient")))
                .andExpect(jsonPath("$.quantity", is("200ml")))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Integer ingredientId = JsonPath.parse(responseJson).read("$.id");

        // Verify the ingredient was created
        mockMvc.perform(get("/api/v1/ingredients/" + ingredientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ingredientId)))
                .andExpect(jsonPath("$.name", is("New Ingredient")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getIngredientsForMe_ReturnsListOfIngredients() throws Exception {
        mockMvc.perform(get("/api/v1/ingredients/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("Test Ingredient")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getIngredientsForUser_ReturnsListOfIngredients() throws Exception {
        mockMvc.perform(get("/api/v1/ingredients/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("Test Ingredient")));
    }
}