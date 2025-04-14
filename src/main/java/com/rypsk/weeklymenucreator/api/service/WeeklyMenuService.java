package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.AutoGenerateWeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WeeklyMenuService {

    WeeklyMenuResponse getWeeklyMenu(Long id);

    WeeklyMenuResponse updateWeeklyMenu(Long id, WeeklyMenuRequest weeklyMenuRequest);

    void deleteWeeklyMenu(Long id);

    WeeklyMenuResponse createWeeklyMenuForUser(WeeklyMenuRequest request, Long userId);

    List<WeeklyMenuResponse> getWeeklyMenusForUser(Long userId);

    WeeklyMenuResponse autoGenerateWeeklyMenuForUser(AutoGenerateWeeklyMenuRequest request, Long userId);

    WeeklyMenuResponse autoGenerateWeeklyMenuForUser(Long id);

    ResponseEntity<byte[]> exportWeeklyMenu(Long id, String format);

    void sendWeeklyMenuByEmail(Long id);

}
