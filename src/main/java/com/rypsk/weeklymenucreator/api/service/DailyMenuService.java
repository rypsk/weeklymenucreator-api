package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface DailyMenuService {

    DailyMenuResponse createDailyMenuForUserId(DailyMenuRequest request, @Min(1) Long userId);

    List<DailyMenuResponse> getDailyMenusByUserId(@Min(1) Long userId);

    DailyMenuResponse getDailyMenuById(@Min(1) Long id);

    DailyMenuResponse updateDailyMenu(@Min(1) Long id, DailyMenuRequest request);

    void deleteDailyMenu(@Min(1) Long id);
}
