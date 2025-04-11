package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Recipe;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;

public record DishResponse(
        Long id,
        String name,
        String description,
        Recipe recipe,
        FoodType foodType,
        Long userId
) {
}
