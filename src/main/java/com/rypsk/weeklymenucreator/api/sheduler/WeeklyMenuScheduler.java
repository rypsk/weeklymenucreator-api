package com.rypsk.weeklymenucreator.api.sheduler;

import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.EmailService;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

//@Component
public class WeeklyMenuScheduler {

    private final WeeklyMenuService weeklyMenuService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public WeeklyMenuScheduler(WeeklyMenuService weeklyMenuService, UserRepository userRepository, EmailService emailService) {
        this.weeklyMenuService = weeklyMenuService;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "1 * * * * *")
    public void autoGenerateAndSendByEmailWeeklyMenu() {
        System.out.println("********* autoGenerateAndSendByEmailWeeklyMenu START");
        List<User> users = userRepository.findAllByIsAutoEmailEnabled(true);
        for (User user : users) {
            WeeklyMenuResponse weeklyMenu = weeklyMenuService.autoGenerateWeeklyMenuForUser(user.getId());
            emailService.sendWeeklyMenuByEmail(weeklyMenu.id(), user);
        }
    }
}
