package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.repository.DailyMenuRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.DailyMenuService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyMenuServiceImpl implements DailyMenuService {

    private final DailyMenuRepository dailyMenuRepository;
    private final UserRepository userRepository;

    public DailyMenuServiceImpl(DailyMenuRepository dailyMenuRepository, UserRepository userRepository) {
        this.dailyMenuRepository = dailyMenuRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public DailyMenuResponse createDailyMenuForUserId(DailyMenuRequest request, Long userId) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        DailyMenu dailyMenu = new DailyMenu();
        dailyMenu.setUser(user);
        dailyMenu.setDayOfWeek(request.dayOfWeek());
        dailyMenu.setDishes(request.dishIds());

        return mapToResponse(dailyMenuRepository.save(dailyMenu));
    }

    @Override
    public List<DailyMenuResponse> getDailyMenusByUserId(Long userId) {
        return dailyMenuRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DailyMenuResponse getDailyMenuById(Long id) {
        DailyMenu dailyMenu = dailyMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Daily menu not found"));
        return mapToResponse(dailyMenu);
    }

    @Override
    @Transactional
    public DailyMenuResponse updateDailyMenu(Long id, DailyMenuRequest request) {
        DailyMenu existingMenu = dailyMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Daily menu not found"));

        existingMenu.setDayOfWeek(request.dayOfWeek());
        existingMenu.setDishes(request.dishIds());

        DailyMenu updatedMenu = dailyMenuRepository.save(existingMenu);
        return mapToResponse(updatedMenu);
    }

    @Override
    public void deleteDailyMenu(Long id) {
        if (!dailyMenuRepository.existsById(id)) {
            throw new RuntimeException("Daily menu not found");
        }
        dailyMenuRepository.deleteById(id);
    }

    private DailyMenuResponse mapToResponse(DailyMenu dailyMenu) {
        return new DailyMenuResponse(
                dailyMenu.getId(),
                dailyMenu.getUser().getId(),
                dailyMenu.getDayOfWeek(),
                dailyMenu.getDishes()
        );
    }
}
