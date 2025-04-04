package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Receipt;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;

/**
 * DTO for Dish requests, used for creating, updating, and managing Dish entities.
 *
 * @param name        The name of the dish.
 * @param description The description of the dish.
 * @param receipt   The receipt associated with the dish.
 * @param foodType    The food type of the dish (e.g., VEGAN, VEGETARIAN, NON_VEGETARIAN).
 * @param userId      The ID of the user associated with the dish.
 */
public record DishRequest(
        String name,
        String description,
        Receipt receipt,
        FoodType foodType,
        Long userId
) {
}
