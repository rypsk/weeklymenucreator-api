package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Recipe;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;

/**
 * DTO for Dish requests, used for creating, updating, and managing Dish entities.
 *
 * @param name        The name of the dish.
 * @param description The description of the dish.
 * @param recipe   The recipe associated with the dish.
 * @param foodType    The food type of the dish (e.g., VEGAN, VEGETARIAN, NON_VEGETARIAN).
 * @param userId      The ID of the user associated with the dish.
 */
public record DishRequest(
        String name,
        String description,
        Recipe recipe,
        FoodType foodType,
        Long userId
) {
}
