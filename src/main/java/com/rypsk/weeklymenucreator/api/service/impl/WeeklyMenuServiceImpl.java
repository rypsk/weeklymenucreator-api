package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.entity.WeeklyMenu;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.repository.WeeklyMenuRepository;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeeklyMenuServiceImpl implements WeeklyMenuService {

    private final WeeklyMenuRepository weeklyMenuRepository;
    private final UserRepository userRepository;

    public WeeklyMenuServiceImpl(WeeklyMenuRepository weeklyMenuRepository, UserRepository userRepository) {
        this.weeklyMenuRepository = weeklyMenuRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public WeeklyMenuResponse createWeeklyMenuForUserId(WeeklyMenuRequest weeklyMenuRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        var weeklyMenu = new WeeklyMenu();
        weeklyMenu.setUser(user);
        weeklyMenu.setStartDate(weeklyMenuRequest.startDate());
        weeklyMenu.setEndDate(weeklyMenuRequest.endDate());
        weeklyMenu.setDailyMenus(weeklyMenuRequest.dailyMenus());

        return mapToResponse(weeklyMenuRepository.save(weeklyMenu));
    }

    @Override
    public List<WeeklyMenuResponse> getWeeklyMenusByUserId(Long userId) {
        return weeklyMenuRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WeeklyMenuResponse getWeeklyMenuById(Long id) {
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        return mapToResponse(weeklyMenu);
    }

    @Override
    public WeeklyMenuResponse updateWeeklyMenu(Long id, WeeklyMenuRequest weeklyMenuRequest) {
        WeeklyMenu existingMenu = weeklyMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weekly menu not found."));

        existingMenu.setStartDate(weeklyMenuRequest.startDate());
        existingMenu.setEndDate(weeklyMenuRequest.endDate());
        existingMenu.setDailyMenus(weeklyMenuRequest.dailyMenus());

        WeeklyMenu updatedMenu = weeklyMenuRepository.save(existingMenu);
        return mapToResponse(updatedMenu);
    }

    @Override
    public void deleteWeeklyMenu(Long id) {
        if (!weeklyMenuRepository.existsById(id)) {
            throw new RuntimeException("Weekly menu not found.");
        }
        weeklyMenuRepository.deleteById(id);
    }

    private WeeklyMenuResponse mapToResponse(WeeklyMenu weeklyMenu) {
        return new WeeklyMenuResponse(
                weeklyMenu.getId(),
                weeklyMenu.getUser().getId(),
                weeklyMenu.getStartDate(),
                weeklyMenu.getEndDate(),
                weeklyMenu.getDailyMenus()
        );
    }
}
