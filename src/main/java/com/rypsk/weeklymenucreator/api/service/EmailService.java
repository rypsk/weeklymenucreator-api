package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.Attachment;
import jakarta.mail.MessagingException;

import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String body, List<Attachment> attachments) throws MessagingException;
}
