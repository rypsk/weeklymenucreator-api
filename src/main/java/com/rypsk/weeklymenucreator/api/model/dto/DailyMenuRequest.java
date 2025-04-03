package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;

import java.util.List;

public record DailyMenuRequest(Long userId, DayOfWeek dayOfWeek, List<Dish> dishIds) {
}
