package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.AuthResponse;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignInRequest;
import com.rypsk.weeklymenucreator.api.model.dto.AuthSignUpRequest;
import com.rypsk.weeklymenucreator.api.security.UserDetailServiceImpl;
import com.rypsk.weeklymenucreator.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserDetailServiceImpl userDetailService;

    public AuthController(AuthService authService, UserDetailServiceImpl userDetailService) {
        this.authService = authService;
        this.userDetailService = userDetailService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestBody @Valid AuthSignInRequest authSignInRequest) {
        return new ResponseEntity<>(this.userDetailService.signIn(authSignInRequest), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid AuthSignUpRequest authSignUpRequest) {
        return new ResponseEntity<>(this.userDetailService.signUp(authSignUpRequest), HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("code") String code) {
        return authService.verifyAccount(code);
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/secured")
    public String helloWorldSecured() {
        return "Hello World Secured";
    }
}
