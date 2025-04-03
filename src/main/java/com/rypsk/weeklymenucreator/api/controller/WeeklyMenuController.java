package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weekly-menu")
public class WeeklyMenuController {

    private static final String ID = "id";

    private final WeeklyMenuService weeklyMenuService;

    public WeeklyMenuController(WeeklyMenuService weeklyMenuService) {
        this.weeklyMenuService = weeklyMenuService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeeklyMenuResponse> getWeeklyMenuById(@PathVariable @Min(1) Long id) {
        try {
            WeeklyMenuResponse response = weeklyMenuService.getWeeklyMenuById(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public WeeklyMenuResponse updateWeeklyMenu(@PathVariable @Min(1) Long id, @RequestBody WeeklyMenuRequest request) {
        return weeklyMenuService.updateWeeklyMenu(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteWeeklyMenu(@PathVariable @Min(1) Long id) {
        weeklyMenuService.deleteWeeklyMenu(id);
    }

    @Transactional
    @PostMapping("/user/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public WeeklyMenuResponse createWeeklyMenuForUserId(@RequestBody WeeklyMenuRequest request, @PathVariable(ID) @Min(1) Long userId) {
        return weeklyMenuService.createWeeklyMenuForUserId(request, userId);
    }

    @GetMapping("/user/{id}")
    public List<WeeklyMenuResponse> getWeeklyMenusByUserId(@PathVariable(ID) @Min(1) Long userId) {
        return weeklyMenuService.getWeeklyMenusByUserId(userId);
    }


}
