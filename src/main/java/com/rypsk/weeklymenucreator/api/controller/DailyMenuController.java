package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuResponse;
import com.rypsk.weeklymenucreator.api.service.DailyMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/daily-menus")
@Tag(name = "Daily Menu", description = "Daily menu management APIs")
public class DailyMenuController {

    private static final String ID = "id";
    private final DailyMenuService dailyMenuService;

    public DailyMenuController(DailyMenuService dailyMenuService){
        this.dailyMenuService = dailyMenuService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a daily menu by ID", description = "Returns a daily menu by its ID")
    public ResponseEntity<DailyMenuResponse> getDailyMenu(
            @Parameter(description = "Daily menu ID", required = true)
            @PathVariable(ID) @Min(1) Long id) {
        return ResponseEntity.ok(dailyMenuService.getDailyMenu(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a daily menu", description = "Updates a daily menu and returns it")
    public ResponseEntity<DailyMenuResponse> updateDailyMenu(
            @Parameter(description = "Daily menu ID", required = true)
            @PathVariable(ID) @Min(1) Long id,
            @Parameter(description = "Updated daily menu details", required = true)
            @RequestBody DailyMenuRequest request) {
        return ResponseEntity.ok(dailyMenuService.updateDailyMenu(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a daily menu", description = "Deletes a daily menu by its ID")
    public ResponseEntity<Void> deleteDailyMenu(
            @Parameter(description = "Daily menu ID", required = true)
            @PathVariable(ID) @Min(1) Long id) {
        dailyMenuService.deleteDailyMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/me")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a daily menu for current user", description = "Creates a new daily menu for the current user and returns it")
    public ResponseEntity<DailyMenuResponse> createDailyMenuForMe(
            @Parameter(description = "Daily menu details", required = true)
            @RequestBody DailyMenuRequest request) {
        return ResponseEntity.ok(dailyMenuService.createDailyMenuForMe(request));
    }

    @PostMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a daily menu for a user", description = "Creates a new daily menu for a specific user and returns it")
    public ResponseEntity<DailyMenuResponse> createDailyMenuForUser(
            @Parameter(description = "Daily menu details", required = true)
            @RequestBody DailyMenuRequest request,
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(dailyMenuService.createDailyMenuForUser(request, userId));
    }

    @GetMapping("/users/me")
    @Operation(summary = "Get daily menus for current user", description = "Returns all daily menus for the current user")
    public ResponseEntity<List<DailyMenuResponse>> getDailyMenusForMe() {
        return ResponseEntity.ok(dailyMenuService.getDailyMenusForMe());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get daily menus for a user", description = "Returns all daily menus for a specific user")
    public ResponseEntity<List<DailyMenuResponse>> getDailyMenusForUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(dailyMenuService.getDailyMenusForUser(userId));
    }

}
