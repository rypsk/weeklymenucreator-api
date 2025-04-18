package com.rypsk.weeklymenucreator.api.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record WeeklyMenuRequest(
        @NotNull(message = "Start Date is required.")
        LocalDate startDate,
        @NotNull(message = "End Date is required.")
        LocalDate endDate,
        @NotEmpty(message = "At leas one Daily Menu must be included.")
        List<Long> dailyMenuIds) {
}
