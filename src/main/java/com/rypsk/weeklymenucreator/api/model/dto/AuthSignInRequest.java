package com.rypsk.weeklymenucreator.api.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthSignInRequest(@NotBlank(message = "Username is required.") String username,
                                @NotBlank(message = "Password is required.") String password) {
}
