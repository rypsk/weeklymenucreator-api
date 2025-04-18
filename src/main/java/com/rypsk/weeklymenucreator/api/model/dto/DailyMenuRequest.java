package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DailyMenuRequest(
        @NotNull(message = "User ID is required.")
        Long userId,
        @NotNull(message = "Day Of Week is required.")
        DayOfWeek dayOfWeek,
        @NotEmpty(message = "At least one dish must be included.")
        List<Long> dishIds) {
}
