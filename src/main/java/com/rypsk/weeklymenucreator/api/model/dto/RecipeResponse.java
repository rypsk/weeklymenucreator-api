package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Ingredient;
import com.rypsk.weeklymenucreator.api.model.enumeration.Difficulty;
import com.rypsk.weeklymenucreator.api.model.enumeration.Season;

import java.util.List;

public record RecipeResponse(
        Long id,
        String name,
        String description,
        List<Ingredient> ingredients,
        Difficulty difficulty,
        List<Season> season,
        boolean isPublic,
        String createdBy
) {
}
