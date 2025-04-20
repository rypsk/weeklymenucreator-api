package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.AutoGenerateWeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;

import java.util.List;

public interface WeeklyMenuService {

    WeeklyMenuResponse getWeeklyMenu(Long id);

    WeeklyMenuResponse updateWeeklyMenu(Long id, WeeklyMenuRequest weeklyMenuRequest);

    void deleteWeeklyMenu(Long id);

    WeeklyMenuResponse createWeeklyMenuForMe(WeeklyMenuRequest request);

    WeeklyMenuResponse createWeeklyMenuForUser(WeeklyMenuRequest request, Long userId);

    List<WeeklyMenuResponse> getWeeklyMenusForMe();

    List<WeeklyMenuResponse> getWeeklyMenusForUser(Long userId);

    WeeklyMenuResponse autoGenerateWeeklyMenuForMe(AutoGenerateWeeklyMenuRequest request);

    WeeklyMenuResponse autoGenerateWeeklyMenuForUser(AutoGenerateWeeklyMenuRequest request, Long userId);

    WeeklyMenuResponse autoGenerateWeeklyMenuForUser(Long id);


}
