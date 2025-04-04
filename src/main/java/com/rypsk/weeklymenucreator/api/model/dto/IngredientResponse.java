package com.rypsk.weeklymenucreator.api.model.dto;

public record IngredientResponse(
        Long id,
        String name,
        String quantity,
        Long userId
) {
}
