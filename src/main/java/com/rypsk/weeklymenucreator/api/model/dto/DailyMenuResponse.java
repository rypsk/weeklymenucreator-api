package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;

import java.util.List;

public record DailyMenuResponse(Long id, Long userId, DayOfWeek dayOfWeek, List<Dish> dishes) {
}
