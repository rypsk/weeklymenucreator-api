package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<String> verifyAccount(String code) {
        return userRepository.findByVerificationCode(code)
                .map(user -> {
                    user.setEnabled(true);
                    user.setVerificationCode(null);
                    userRepository.save(user);
                    return ResponseEntity.ok("Account verified successfully. You can now log in.");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification code."));
    }

}
