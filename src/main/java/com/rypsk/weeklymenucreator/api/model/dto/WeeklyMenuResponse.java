package com.rypsk.weeklymenucreator.api.model.dto;

import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;

import java.time.LocalDate;
import java.util.List;

public record WeeklyMenuResponse(Long id, Long userId, LocalDate startDate, LocalDate endDate,
                                 List<DailyMenu> dailyMenus) {

}
