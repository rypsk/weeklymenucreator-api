package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public record AutoGenerateWeeklyMenuRequest(LocalDate startDate,
                                            LocalDate endDate,
                                            Set<DishType> dishTypes,
                                            Map<FoodType, Integer> foodTypePreferences,
                                            DietType dietType) {
}
