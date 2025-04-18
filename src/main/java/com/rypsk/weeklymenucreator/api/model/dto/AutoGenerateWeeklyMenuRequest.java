package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public record AutoGenerateWeeklyMenuRequest(
        @NotNull(message = "Start Date is required.")
        LocalDate startDate,
        @NotNull(message = "End Date is required.")
        LocalDate endDate,
        @NotEmpty(message = "At least one dish type is required.")
        Set<DishType> dishTypes,
        @Nullable
        Map<FoodType, Integer> preferences,
        @Nullable
        DietType dietType,
        boolean allowRepeat) {
}
