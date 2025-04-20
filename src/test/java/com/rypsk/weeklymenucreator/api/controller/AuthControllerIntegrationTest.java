package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignInRequest;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignUpRequest;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.Role;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthControllerIntegrationTest {

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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthSignInRequest validSignInRequest;
    private AuthSignUpRequest validSignUpRequest;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        userRepository.deleteAll();

        // Setup test data
        validSignInRequest = new AuthSignInRequest("testuser", "password123");
        validSignUpRequest = new AuthSignUpRequest("testuser", "password123", "test@example.com");

        // Create a test user for sign-in tests
        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("password123"))
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .build();
        userRepository.save(user);
    }

    @Test
    void signIn_WithValidCredentials_ReturnsToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSignInRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.message", containsString("successfully")))
                .andExpect(jsonPath("$.jwt", not(emptyString())))
                .andExpect(jsonPath("$.status", is(true)));
    }

    @Test
    void signIn_WithInvalidCredentials_ReturnsBadRequest() throws Exception {
        AuthSignInRequest invalidRequest = new AuthSignInRequest("testuser", "wrongpassword");

        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void signUp_WithValidData_CreatesUser() throws Exception {
        // Delete the test user created in setUp
        userRepository.deleteAll();

        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSignUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.message", containsString("successfully")))
                .andExpect(jsonPath("$.status", is(false))); // Status is false until account is verified
    }

    @Test
    void signUp_WithExistingUsername_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSignUpRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void helloWorld_ReturnsMessage() throws Exception {
        mockMvc.perform(get("/api/v1/auth/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    // Note: Testing secured endpoint would require authentication token
    // This is a simplified test that expects a 401 Unauthorized response
    @Test
    void helloWorldSecured_WithoutAuth_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/auth/secured"))
                .andExpect(status().isUnauthorized());
    }
}