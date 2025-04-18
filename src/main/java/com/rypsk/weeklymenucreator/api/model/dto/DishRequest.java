package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Recipe;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DishRequest(
        @NotBlank(message = "Dish name is required")
        @Size(max = 100, message = "Name can be at most 100 characters")
        String name,

        @Size(max = 500, message = "Description can be at most 500 characters")
        String description,

        @NotNull(message = "Recipe is required")
        @Valid Recipe recipe,

        @NotNull(message = "Food type is required")
        FoodType foodType,

        @NotNull(message = "User ID is required")
        Long userId
) {
}
