package com.rypsk.weeklymenucreator.api.model.dto;

public record IngredientRequest(
        String name,
        String quantity,
        String unit
) {
}
