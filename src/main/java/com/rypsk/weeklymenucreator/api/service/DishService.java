package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DishResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface DishService {
    DishResponse getDishById(@Min(1) Long id);

    DishResponse updateDish(@Min(1) Long id, DishRequest request);

    void deleteDish(@Min(1) Long id);

    DishResponse createDishForUserId(DishRequest request);

    List<DishResponse> getDishesByUserId(@Min(1) Long userId);
}
