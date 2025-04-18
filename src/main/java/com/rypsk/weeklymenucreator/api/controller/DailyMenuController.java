package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuResponse;
import com.rypsk.weeklymenucreator.api.service.DailyMenuService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/daily-menus")
public class DailyMenuController {

    private static final String ID = "id";
    private final DailyMenuService dailyMenuService;

    public DailyMenuController(DailyMenuService dailyMenuService){
        this.dailyMenuService = dailyMenuService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyMenuResponse> getDailyMenu(@PathVariable(ID) @Min(1) Long id) {
        return ResponseEntity.ok(dailyMenuService.getDailyMenu(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyMenuResponse> updateDailyMenu(@PathVariable(ID) @Min(1) Long id, @RequestBody DailyMenuRequest request) {
        return ResponseEntity.ok(dailyMenuService.updateDailyMenu(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDailyMenu(@PathVariable(ID) @Min(1) Long id) {
        dailyMenuService.deleteDailyMenu(id);
        return ResponseEntity.noContent().build();
    }

}
