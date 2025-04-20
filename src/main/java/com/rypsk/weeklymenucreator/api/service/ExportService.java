package com.rypsk.weeklymenucreator.api.service;

import org.springframework.http.ResponseEntity;

public interface ExportService {

    ResponseEntity<byte[]> exportWeeklyMenu(Long id, String format);
}
