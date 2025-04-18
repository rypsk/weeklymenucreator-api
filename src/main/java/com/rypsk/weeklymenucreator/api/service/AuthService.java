package com.rypsk.weeklymenucreator.api.service;

import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> verifyAccount(String code);
}
