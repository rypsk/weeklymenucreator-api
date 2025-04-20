package com.rypsk.weeklymenucreator.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rypsk.weeklymenucreator.api.model.dto.AuthResponse;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignInRequest;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignUpRequest;
import com.rypsk.weeklymenucreator.api.security.UserDetailServiceImpl;
import com.rypsk.weeklymenucreator.api.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@Import(AuthControllerTest.TestConfig.class)
class AuthControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public AuthService authService() {
            return mock(AuthService.class);
        }

        @Bean
        public UserDetailServiceImpl userDetailService() {
            return mock(UserDetailServiceImpl.class);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }

        @Bean
        public AuthController authController(AuthService authService, UserDetailServiceImpl userDetailService) {
            return new AuthController(authService, userDetailService);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    private AuthSignInRequest validSignInRequest;
    private AuthSignUpRequest validSignUpRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        validSignInRequest = new AuthSignInRequest("testuser", "password");
        validSignUpRequest = new AuthSignUpRequest("testuser", "password123", "test@example.com");
        authResponse = new AuthResponse("testuser", "Success", "jwt-token", true);
    }

    @Test
    void signIn_ReturnsAuthResponse() throws Exception {
        when(userDetailService.signIn(any(AuthSignInRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSignInRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.jwt", is("jwt-token")))
                .andExpect(jsonPath("$.status", is(true)));
    }

    @Test
    void signUp_ReturnsAuthResponse() throws Exception {
        when(userDetailService.signUp(any(AuthSignUpRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSignUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.jwt", is("jwt-token")))
                .andExpect(jsonPath("$.status", is(true)));
    }

    @Test
    void verifyAccount_ReturnsSuccessMessage() throws Exception {
        when(authService.verifyAccount(anyString())).thenReturn(
                new ResponseEntity<>("Account verified successfully", HttpStatus.OK));

        mockMvc.perform(get("/api/v1/auth/verify")
                        .param("code", "verification-code"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account verified successfully"));
    }

    @Test
    void helloWorld_ReturnsMessage() throws Exception {
        mockMvc.perform(get("/api/v1/auth/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    @Test
    void helloWorldSecured_ReturnsMessage() throws Exception {
        mockMvc.perform(get("/api/v1/auth/secured"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World Secured"));
    }
}
