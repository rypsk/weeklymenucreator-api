package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;

import java.util.List;

public interface WeeklyMenuService {
    WeeklyMenuResponse createWeeklyMenuForUserId(WeeklyMenuRequest weeklyMenuRequest, Long userId);

    List<WeeklyMenuResponse> getWeeklyMenusByUserId(Long userId);

    WeeklyMenuResponse getWeeklyMenuById(Long id);

    WeeklyMenuResponse updateWeeklyMenu(Long id, WeeklyMenuRequest weeklyMenuRequest);

    void deleteWeeklyMenu(Long id);
}
