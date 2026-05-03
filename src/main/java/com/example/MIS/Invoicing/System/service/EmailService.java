package com.example.MIS.Invoicing.System.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${app.base-url}")
    private String baseUrl;

   public void sendVerificationEmail(String toEmail, String token) {

        String verifyUrl = baseUrl + "/api/auth/verify?token=" + token;

        String htmlBody =
            "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto;'>" +
            "  <h2 style='color: #2d2d2d;'>✅ Verify Your Email</h2>" +
            "  <p>Hello,</p>" +
            "  <p>Thank you for registering on <strong>MIS Invoicing System</strong>.</p>" +
            "  <p>Please click the button below to verify your email:</p>" +
            "  <a href='" + verifyUrl + "' " +
            "     style='background-color: #4CAF50; color: white; padding: 12px 24px;" +
            "            text-decoration: none; border-radius: 5px; display: inline-block;'>" +
            "     Verify Email" +
            "  </a>" +
            "  <p style='margin-top: 20px; color: #888;'>⚠️ This link expires in 24 hours.</p>" +
            "  <p style='color: #888;'>If you did not register, ignore this email.</p>" +
            "  <hr/>" +
            "  <p style='color: #aaa; font-size: 12px;'>MIS Invoicing System Team</p>" +
            "</div>";

        sendEmail(toEmail, "✅ Verify Your Email - MIS Invoicing System", htmlBody);
    }

    public void sendResetPasswordEmail(String toEmail, String token) {

        String resetUrl = baseUrl + "/api/auth/reset-password?token=" + token;

        String htmlBody =
            "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto;'>" +
            "  <h2 style='color: #d9534f;'>🔑 Reset Your Password</h2>" +
            "  <p>Hello,</p>" +
            "  <p>We received a request to reset your password for " +
            "     <strong>MIS Invoicing System</strong>.</p>" +
            "  <p>Click the button below to reset your password:</p>" +
            "  <a href='" + resetUrl + "' " +
            "     style='background-color: #d9534f; color: white; padding: 12px 24px;" +
            "            text-decoration: none; border-radius: 5px; display: inline-block;'>" +
            "     Reset Password" +
            "  </a>" +
            "  <p style='margin-top: 20px; color: #888;'>⚠️ This link expires in 1 hour.</p>" +
            "  <p style='color: #888;'>If you did not request this, ignore this email.</p>" +
            "  <hr/>" +
            "  <p style='color: #aaa; font-size: 12px;'>MIS Invoicing System Team</p>" +
            "</div>";

        sendEmail(toEmail, "🔑 Reset Your Password - MIS Invoicing System", htmlBody);
    }

    private void sendEmail(String toEmail, String subject, String htmlBody) {
        try {
            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to(toEmail)
                    .subject(subject)
                    .html(htmlBody)
                    .build();

            CreateEmailResponse response = resend.emails().send(params);
            System.out.println("✅ Email sent! ID: " + response.getId());

        } catch (ResendException e) {
            System.err.println("❌ Email sending failed: " + e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}