package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.entity.Attachment;
import com.rypsk.weeklymenucreator.api.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
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
}
