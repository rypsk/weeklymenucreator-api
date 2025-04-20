package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.entity.Attachment;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import jakarta.mail.MessagingException;

import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String body, List<Attachment> attachments) throws MessagingException;

    void sendWeeklyMenuByEmail(Long id, User user);
}
