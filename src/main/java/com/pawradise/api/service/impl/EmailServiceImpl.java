package com.pawradise.api.service.impl;

import com.pawradise.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(@Autowired JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Async // This makes the method run in a separate thread
    public void sendOtpEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your Pawradise Verification Code");
            message.setText("Hello,\n" +
                    "\n" +
                    "You recently requested to change the email address for your Pawradise account. Please use the following verification code to confirm this action.\n" +
                    "\n" +
                    "Your verification code is:\n" +
                    otp + "\n" +
                    "\n" +
                    "This code is valid for 10 minutes. For your security, please do not share this code with anyone.\n" +
                    "\n" +
                    "If you did not request this change, you can safely ignore this email.\n" +
                    "\n" +
                    "Thank you,\n" +
                    "The Pawradise Team");

            mailSender.send(message);
        } catch (Exception e) {
            // In a real app, you'd have more robust error handling/logging
            System.err.println("Error sending OTP email: " + e.getMessage());
        }
    }

    @Async
    public void sendAccountDeactivationNotice(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Pawradise Account Deletion Initiated");
        message.setText("Hello " + name + ",\n\nThis is a confirmation that you have initiated the deletion of your Pawradise account."
                + " Your account and data will be permanently anonymized in 15 days.\n\n"
                + "If you change your mind, simply log in to your account within the next 15 days to automatically reactivate it.\n\n"
                + "If you did not request this, please contact our support team immediately.\n\n"
                + "Thank you,\nThe Pawradise Team");
        mailSender.send(message);
    }

    @Async
    public void sendAccountDeletionConfirmation(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Pawradise Account Has Been Deleted");
        message.setText("Hello,\n\nThis is a confirmation that your Pawradise account has been permanently anonymized as per your request.\n\n"
                + "Thank you for being a part of our community.\n\n"
                + "The Pawradise Team");
        mailSender.send(message);
    }
}