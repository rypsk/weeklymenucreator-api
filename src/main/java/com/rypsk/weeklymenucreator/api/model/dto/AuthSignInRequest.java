package com.rypsk.weeklymenucreator.api.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthSignInRequest(@NotBlank String username,
                                @NotBlank String password) {
}
