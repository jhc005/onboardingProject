package com.onboarding.adminUsers.application.commands;

import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.shared.cqrs.CommandHandler;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ForgotPasswordCommandHandler implements CommandHandler<ForgotPasswordCommand, Void> {

    private final AdminUsersRepositoryPort adminUsersPort;
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public Void handle(ForgotPasswordCommand command) {
        Optional<AdminUsers> optional = adminUsersPort.findByEmail(command.getEmail());
        if (optional.isEmpty()) return null;

        AdminUsers admin = optional.get();
        String token = UUID.randomUUID().toString();
        admin.setResetToken(token);
        admin.setResetTokenExpiresAt(LocalDateTime.now().plusHours(1));
        adminUsersPort.save(admin);

        String resetLink = frontendUrl + "/reset-password?token=" + token;
        sendResetEmail(admin.getEmail(), resetLink);
        return null;
    }

    private void sendResetEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Restablecer contraseña - Civica Software");
            helper.setText(buildEmailContent(resetLink), true);
            mailSender.send(message);
        } catch (Exception e) {
            // El error no se propaga para no revelar si el email existe
        }
    }

    private String buildEmailContent(String resetLink) {
        return """
                <!DOCTYPE html><html lang="es">
                <head><meta charset="UTF-8"></head>
                <body style="margin:0;padding:0;background:#f5f5f5;font-family:Arial,sans-serif;">
                  <table width="100%" cellpadding="0" cellspacing="0">
                    <tr><td align="center" style="padding:32px 0;">
                      <table width="600" cellpadding="0" cellspacing="0" style="background:#fff;border-radius:8px;overflow:hidden;">
                        <tr><td style="background:#f05a28;padding:24px 32px;">
                          <span style="color:#fff;font-size:22px;font-weight:700;">civica software</span>
                        </td></tr>
                        <tr><td style="padding:32px;">
                          <p>Hola,</p>
                          <p>Recibimos una solicitud para restablecer la contraseña de tu cuenta de administrador.</p>
                          <p style="text-align:center;margin:32px 0;">
                            <a href=\"""" + resetLink + """
                " style="background:#f05a28;color:#fff;padding:14px 32px;border-radius:8px;text-decoration:none;font-weight:700;font-size:15px;">
                              Restablecer contraseña
                            </a>
                          </p>
                          <p style="color:#999;font-size:12px;">Este enlace expira en 1 hora. Si no solicitaste esto, podés ignorar este email.</p>
                        </td></tr>
                        <tr><td style="background:#fff8f5;padding:16px 32px;color:#888;font-size:12px;">
                          Equipo de People Care - Civica Software
                        </td></tr>
                      </table>
                    </td></tr>
                  </table>
                </body></html>
                """;
    }
}
