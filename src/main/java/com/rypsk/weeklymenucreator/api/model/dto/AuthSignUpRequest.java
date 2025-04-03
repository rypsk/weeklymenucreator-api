package com.rypsk.weeklymenucreator.api.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthSignUpRequest(@NotBlank String username,
                                @NotBlank String password,
                                @NotBlank String email) {
}
