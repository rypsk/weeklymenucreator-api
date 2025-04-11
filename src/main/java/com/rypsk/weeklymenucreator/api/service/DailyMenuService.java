package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface DailyMenuService {

    DailyMenuResponse getDailyMenu(@Min(1) Long id);

    DailyMenuResponse updateDailyMenu(@Min(1) Long id, DailyMenuRequest request);

    void deleteDailyMenu(@Min(1) Long id);

    DailyMenuResponse createDailyMenuForUser(DailyMenuRequest request, Long userId);

    List<DailyMenuResponse> getDailyMenusForUser(Long userId);
}
