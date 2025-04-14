package com.rypsk.weeklymenucreator.api.sheduler;

import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class WeeklyMenuSheduler {

    private final WeeklyMenuService weeklyMenuService;
    private final UserRepository userRepository;

    public WeeklyMenuSheduler(WeeklyMenuService weeklyMenuService, UserRepository userRepository) {
        this.weeklyMenuService = weeklyMenuService;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "1 * * * * *")
    public void autoGenerateAndSendByEmailWeeklyMenu(){
        List<User> users = userRepository.findAllByAutoEmailEnabled(true);
        for (User user : users) {
            WeeklyMenuResponse weeklyMenu = weeklyMenuService.autoGenerateWeeklyMenuForUser(user.getId());
            weeklyMenuService.sendWeeklyMenuByEmail(weeklyMenu.id());
        }
    }
}
