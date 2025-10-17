package com.pawradise.api.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
    void sendAccountDeactivationNotice(String to, String name);
    void sendAccountDeletionConfirmation(String to, String name);
}
