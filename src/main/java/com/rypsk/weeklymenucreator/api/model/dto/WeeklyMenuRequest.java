package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;

import java.time.LocalDate;
import java.util.List;

public record WeeklyMenuRequest(LocalDate startDate,
                                LocalDate endDate,
                                List<DailyMenu> dailyMenus) {
}
