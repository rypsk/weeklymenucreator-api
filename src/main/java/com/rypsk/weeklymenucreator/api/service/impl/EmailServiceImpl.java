package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.entity.Attachment;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.entity.WeeklyMenu;
import com.rypsk.weeklymenucreator.api.repository.WeeklyMenuRepository;
import com.rypsk.weeklymenucreator.api.service.EmailService;
import com.rypsk.weeklymenucreator.api.service.ExportService;
import com.rypsk.weeklymenucreator.api.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final WeeklyMenuRepository weeklyMenuRepository;
    private final ExportService exportService;

    public EmailServiceImpl(JavaMailSender mailSender, UserService userService,
                            WeeklyMenuRepository weeklyMenuRepository, ExportService exportService) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.weeklyMenuRepository = weeklyMenuRepository;
        this.exportService = exportService;
    }

    @Override
    public void sendEmail(String to, String subject, String body, List<Attachment> attachments) throws MessagingException {
        if (attachments == null) {
            attachments = Collections.emptyList();
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false);
        helper.setFrom(emailFrom);

        for (Attachment attachment : attachments) {
            helper.addAttachment(
                    attachment.getFileName(),
                    () -> new ByteArrayInputStream(attachment.getContent()),
                    attachment.getContentType()
            );
        }

        mailSender.send(message);
    }

    @Override
    public void sendWeeklyMenuByEmail(Long id, User user) {
        if (user == null) {
            user = userService.getCurrentUser();
        }
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot send by email this weeklyMenu.");
        }

        byte[] pdfBytes = exportService.exportWeeklyMenu(id, "pdf").getBody();

        Attachment attachment = new Attachment("weekly-menu-" + weeklyMenu.getId() + ".pdf", "application/pdf", pdfBytes);

        String subject = "Your Weekly Menu (ID: " + weeklyMenu.getId() + ")";
        String body = "Here is your weekly menu as requested.\n\n";

        body += exportService.exportWeeklyMenu(id, "csv").getBody();

        try {
            sendEmail(user.getEmail(), subject, body, java.util.List.of(attachment));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
