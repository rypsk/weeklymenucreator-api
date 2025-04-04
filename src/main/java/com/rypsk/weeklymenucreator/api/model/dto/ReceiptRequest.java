package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Ingredient;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.Difficulty;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.Season;

import java.util.List;

public record ReceiptRequest(
        String name,
        String description, List<Ingredient> ingredients,
        Difficulty difficulty,
        List<Season> season,
        DishType dishType,
        DietType dietType ) {
}
