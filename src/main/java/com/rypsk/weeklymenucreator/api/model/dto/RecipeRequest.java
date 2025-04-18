package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Ingredient;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.Difficulty;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.Season;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RecipeRequest(
        @NotBlank(message = "Recipe name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "Description can be up to 500 characters")
        String description,

        @NotEmpty(message = "At least one ingredient is required")
        List<@Valid Ingredient> ingredients,

        @NotNull(message = "Difficulty is required")
        Difficulty difficulty,

        @NotEmpty(message = "At least one season must be selected")
        List<Season> season,

        @NotNull(message = "Dish type is required")
        DishType dishType,

        @NotNull(message = "Diet type is required")
        DietType dietType,

        boolean isPublic
) {
}
