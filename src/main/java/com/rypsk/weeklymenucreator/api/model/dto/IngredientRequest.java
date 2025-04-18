package com.rypsk.weeklymenucreator.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IngredientRequest(
        @NotBlank(message = "Ingredient name is required")
        @Size(max = 100, message = "Name can be at most 100 characters")
        String name,

        @NotBlank(message = "Quantity is required")
        @Size(max = 20, message = "Quantity can be at most 20 characters")
        String quantity,

        @NotBlank(message = "Unit is required")
        @Size(max = 20, message = "Unit can be at most 20 characters")
        String unit
) {
}
