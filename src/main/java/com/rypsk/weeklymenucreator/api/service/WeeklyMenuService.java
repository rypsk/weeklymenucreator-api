package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;

import java.util.List;

public interface WeeklyMenuService {
    WeeklyMenuResponse createWeeklyMenuForUser(WeeklyMenuRequest weeklyMenuRequest, Long userId);

    List<WeeklyMenuResponse> getWeeklyMenusByUser(Long userId);

    WeeklyMenuResponse getWeeklyMenu(Long id);

    WeeklyMenuResponse updateWeeklyMenu(Long id, WeeklyMenuRequest weeklyMenuRequest);

    void deleteWeeklyMenu(Long id);
}
