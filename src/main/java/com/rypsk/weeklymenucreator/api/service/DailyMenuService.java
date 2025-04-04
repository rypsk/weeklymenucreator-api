package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface DailyMenuService {

    DailyMenuResponse createDailyMenuForUser(DailyMenuRequest request, @Min(1) Long userId);

    List<DailyMenuResponse> getDailyMenusByUser(@Min(1) Long userId);

    DailyMenuResponse getDailyMenu(@Min(1) Long id);

    DailyMenuResponse updateDailyMenu(@Min(1) Long id, DailyMenuRequest request);

    void deleteDailyMenu(@Min(1) Long id);
}
