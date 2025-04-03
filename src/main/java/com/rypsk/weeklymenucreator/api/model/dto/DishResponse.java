package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Receipt;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;

public record DishResponse(
        Long id,
        String name,
        String description,
        Receipt receipt,
        FoodType foodType,
        Long userId
) {
}
