package com.leavemgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * EmailNotificationService - Sends email notifications when leave requests are approved or rejected
 */
@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendLeaveDecisionEmail(String recipientEmail, String employeeName, String status, String comment) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Leave Request " + status);
        message.setText(buildMessageBody(employeeName, status, comment));

        mailSender.send(message);
    }

    private String buildMessageBody(String employeeName, String status, String comment) {
        StringBuilder builder = new StringBuilder();
        builder.append("Hello ").append(employeeName).append(",\n\n");
        builder.append("Your leave request has been ").append(status.toLowerCase()).append(".\n\n");
        if (comment != null && !comment.isBlank()) {
            builder.append("Manager comment: \"").append(comment).append("\"\n\n");
        }
        builder.append("If you have any questions, please reach out to your manager.\n\n");
        builder.append("Regards,\nEmployee Leave Management System");
        return builder.toString();
    }
}
