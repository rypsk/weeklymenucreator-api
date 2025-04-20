package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.AuthResponse;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignInRequest;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignUpRequest;
import com.rypsk.weeklymenucreator.api.security.UserDetailServiceImpl;
import com.rypsk.weeklymenucreator.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;
    private final UserDetailServiceImpl userDetailService;

    public AuthController(AuthService authService, UserDetailServiceImpl userDetailService) {
        this.authService = authService;
        this.userDetailService = userDetailService;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in", description = "Authenticates a user and returns a token")
    public ResponseEntity<AuthResponse> signIn(
            @Parameter(description = "Sign in credentials", required = true)
            @RequestBody @Valid AuthSignInRequest authSignInRequest) {
        return new ResponseEntity<>(this.userDetailService.signIn(authSignInRequest), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Sign up", description = "Registers a new user and returns a token")
    public ResponseEntity<AuthResponse> signUp(
            @Parameter(description = "Sign up details", required = true)
            @RequestBody @Valid AuthSignUpRequest authSignUpRequest) {
        return new ResponseEntity<>(this.userDetailService.signUp(authSignUpRequest), HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    @Operation(summary = "Verify account", description = "Verifies a user account using a verification code")
    public ResponseEntity<String> verifyAccount(
            @Parameter(description = "Verification code", required = true)
            @RequestParam("code") String code) {
        return authService.verifyAccount(code);
    }

    @GetMapping("/hello")
    @Operation(summary = "Hello world", description = "Returns a hello world message")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/secured")
    @Operation(summary = "Secured hello world", description = "Returns a hello world message (requires authentication)")
    public String helloWorldSecured() {
        return "Hello World Secured";
    }
}
