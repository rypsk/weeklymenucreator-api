package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.AutoGenerateWeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.service.EmailService;
import com.rypsk.weeklymenucreator.api.service.ExportService;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weekly-menus")
@Tag(name = "Weekly Menu", description = "Weekly menu management APIs")
public class WeeklyMenuController {

    private static final String ID = "id";

    private final WeeklyMenuService weeklyMenuService;
    private final ExportService exportService;
    private final EmailService emailService;

    public WeeklyMenuController(WeeklyMenuService weeklyMenuService, ExportService exportService, EmailService emailService) {
        this.weeklyMenuService = weeklyMenuService;
        this.exportService = exportService;
        this.emailService = emailService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a weekly menu by ID", description = "Returns a weekly menu by its ID")
    public ResponseEntity<WeeklyMenuResponse> getWeeklyMenu(
            @Parameter(description = "Weekly menu ID", required = true)
            @PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(weeklyMenuService.getWeeklyMenu(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a weekly menu", description = "Updates a weekly menu and returns it")
    public WeeklyMenuResponse updateWeeklyMenu(
            @Parameter(description = "Weekly menu ID", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "Updated weekly menu details", required = true)
            @RequestBody WeeklyMenuRequest request) {
        return weeklyMenuService.updateWeeklyMenu(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a weekly menu", description = "Deletes a weekly menu by its ID")
    public void deleteWeeklyMenu(
            @Parameter(description = "Weekly menu ID", required = true)
            @PathVariable @Min(1) Long id) {
        weeklyMenuService.deleteWeeklyMenu(id);
    }

    @GetMapping("/{id}/export/{format}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Export a weekly menu", description = "Exports a weekly menu in the specified format")
    public ResponseEntity<byte[]> exportWeeklyMenu(
            @Parameter(description = "Weekly menu ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Export format (e.g., pdf, xlsx)", required = true)
            @PathVariable String format) {
        return exportService.exportWeeklyMenu(id, format);
    }

    @PostMapping("/{id}/email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Send weekly menu by email", description = "Sends a weekly menu to the user's email")
    public ResponseEntity<String> sendWeeklyMenuByEmail(
            @Parameter(description = "Weekly menu ID", required = true)
            @PathVariable Long id) {
        emailService.sendWeeklyMenuByEmail(id, null);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/me/auto-generate")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Auto-generate a weekly menu", description = "Automatically generates a weekly menu for the current user based on preferences")
    public ResponseEntity<WeeklyMenuResponse> autoGenerateWeeklyMenu(
            @Parameter(description = "Auto-generation parameters", required = true)
            @RequestBody AutoGenerateWeeklyMenuRequest request) {
        WeeklyMenuResponse generatedMenu = weeklyMenuService.autoGenerateWeeklyMenuForMe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(generatedMenu);
    }

    @PostMapping("/users/me")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a weekly menu for current user", description = "Creates a new weekly menu for the current user and returns it")
    public ResponseEntity<WeeklyMenuResponse> createWeeklyMenuForMe(
            @Parameter(description = "Weekly menu details", required = true)
            @RequestBody WeeklyMenuRequest request) {
        return ResponseEntity.ok(weeklyMenuService.createWeeklyMenuForMe(request));
    }

    @PostMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a weekly menu for a user", description = "Creates a new weekly menu for a specific user and returns it")
    public ResponseEntity<WeeklyMenuResponse> createWeeklyMenuForUser(
            @Parameter(description = "Weekly menu details", required = true)
            @RequestBody WeeklyMenuRequest request,
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(weeklyMenuService.createWeeklyMenuForUser(request, userId));
    }

    @GetMapping("/users/me")
    @Operation(summary = "Get weekly menus for current user", description = "Returns all weekly menus for the current user")
    public ResponseEntity<List<WeeklyMenuResponse>> getWeeklyMenusForMe() {
        return ResponseEntity.ok(weeklyMenuService.getWeeklyMenusForMe());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get weekly menus for a user", description = "Returns all weekly menus for a specific user")
    public ResponseEntity<List<WeeklyMenuResponse>> getWeeklyMenusForUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(weeklyMenuService.getWeeklyMenusForUser(userId));
    }

}
