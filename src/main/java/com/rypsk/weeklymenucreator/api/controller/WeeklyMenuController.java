package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weekly-menus")
public class WeeklyMenuController {

    private static final String ID = "id";

    private final WeeklyMenuService weeklyMenuService;

    public WeeklyMenuController(WeeklyMenuService weeklyMenuService) {
        this.weeklyMenuService = weeklyMenuService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeeklyMenuResponse> getWeeklyMenu(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(weeklyMenuService.getWeeklyMenu(id));
    }

    @PutMapping("/{id}")
    public WeeklyMenuResponse updateWeeklyMenu(@PathVariable @Min(1) Long id, @RequestBody WeeklyMenuRequest request) {
        return weeklyMenuService.updateWeeklyMenu(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteWeeklyMenu(@PathVariable @Min(1) Long id) {
        weeklyMenuService.deleteWeeklyMenu(id);
    }



}
