package com.onboarding.notifications.application;

import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.notifications.domain.model.Notifications;
import com.onboarding.notifications.domain.valueobject.NotificationType;
import com.onboarding.notifications.domain.valueobject.NotificationStatus;
import com.onboarding.notifications.domain.outport.NotificationsRepositoryPort;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsService {

    private final JavaMailSender mailSender;
    private final NotificationsRepositoryPort notificationsPort;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.mail.admin}")
    private String adminEmail;

    public void sendAccessLink(String processId, Candidates candidate, String rawToken) {
        String link = frontendUrl + "/onboarding?token=" + rawToken;
        String subject = "Bienvenido/a al proceso de incorporacion a Civica Software";
        String content =
                "<p>Hola <strong>" + candidate.getFirstName() + " " + candidate.getLastName() + "</strong>,</p>" +
                "<p>Has sido dado de alta en nuestro proceso de incorporacion.<br>" +
                "Por favor, accede al siguiente enlace para completar tu documentacion:</p>" +
                "<p style=\"text-align:center;margin:32px 0;\">" +
                "  <a href=\"" + link + "\" style=\"background:#f05a28;color:#fff;padding:14px 32px;border-radius:8px;text-decoration:none;font-weight:700;font-size:15px;\">Acceder al formulario</a>" +
                "</p>" +
                "<p style=\"color:#999;font-size:12px;\">Este enlace es personal e intransferible. Caduca en 30 dias.</p>";

        sendAndRecord(processId, candidate.getEmail(), subject, content, NotificationType.ACCESS_LINK);
    }

    public void sendFormSubmitted(String processId, String candidateFullName) {
        String subject = "Nuevo formulario completado - " + candidateFullName;
        String content =
                "<p>Se ha recibido la documentacion de <strong>" + candidateFullName + "</strong> y esta pendiente de revision.</p>" +
                "<p>Accede al panel de administracion para revisar el expediente.</p>";

        sendAndRecord(processId, adminEmail, subject, content, NotificationType.FORM_SUBMITTED);
    }

    public void sendValidated(String processId, String candidateEmail, String candidateFullName) {
        String subject = "Tu documentacion ha sido validada";
        String content =
                "<p>Hola <strong>" + candidateFullName + "</strong>,</p>" +
                "<p>Tu documentacion ha sido revisada y <strong style=\"color:#2e7d32;\">validada correctamente</strong> por el equipo de People Care.</p>" +
                "<p>En breve recibiras informacion sobre los proximos pasos.</p>";

        sendAndRecord(processId, candidateEmail, subject, content, NotificationType.VALIDATED);
    }

    public void sendRejected(String processId, String candidateEmail, String candidateFullName, String notes, String rawToken) {
        String link = frontendUrl + "/onboarding?token=" + rawToken;
        String subject = "Revision de tu documentacion - Accion requerida";
        String notesText = notes != null ? notes : "Consulta con el equipo de People Care.";
        String content =
                "<p>Hola <strong>" + candidateFullName + "</strong>,</p>" +
                "<p>Tras revisar tu documentacion, hemos detectado algunos problemas que requieren tu atencion:</p>" +
                "<div style=\"background:#fff3f0;border-left:4px solid #f05a28;padding:12px 16px;border-radius:4px;margin:16px 0;\">" +
                notesText +
                "</div>" +
                "<p>Por favor, accede al siguiente enlace para corregir unicamente los campos indicados. El resto de informacion que ya enviaste se mantendra tal como estaba:</p>" +
                "<p style=\"text-align:center;margin:32px 0;\">" +
                "  <a href=\"" + link + "\" style=\"background:#f05a28;color:#fff;padding:14px 32px;border-radius:8px;text-decoration:none;font-weight:700;font-size:15px;\">Corregir documentacion</a>" +
                "</p>" +
                "<p style=\"color:#999;font-size:12px;\">Este enlace es personal e intransferible. Caduca en 30 dias.</p>";

        sendAndRecord(processId, candidateEmail, subject, content, NotificationType.REJECTED);
    }

    private void sendAndRecord(String processId, String to, String subject, String content,
                               NotificationType type) {
        NotificationStatus status = NotificationStatus.SENT;
        String errorMessage = null;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(buildHtmlTemplate(content), true);
            mailSender.send(message);
            log.info("Email enviado a {} - tipo: {}", to, type);
        } catch (Exception e) {
            log.error("Error enviando email a {} - tipo: {}: {}", to, type, e.getMessage());
            status = NotificationStatus.FAILED;
            errorMessage = e.getMessage();
        }

        Notifications notification = new Notifications();
        notification.setOnboardingProcessId(processId);
        notification.setType(type);
        notification.setRecipientEmail(to);
        notification.setStatus(status);
        notification.setErrorMessage(errorMessage);

        notificationsPort.save(notification);
    }

    private String buildHtmlTemplate(String content) {
        return "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"></head>" +
               "<body style=\"margin:0;padding:0;background:#f5f5f5;font-family:Arial,sans-serif;\">" +
               "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#f5f5f5;padding:32px 0;\">" +
               "<tr><td align=\"center\">" +
               "<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#ffffff;border-radius:10px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.08);\">" +
               "<tr><td style=\"background:#f05a28;padding:24px 32px;\">" +
               "<span style=\"color:#fff;font-size:22px;font-weight:700;letter-spacing:1px;\">civica software</span>" +
               "</td></tr>" +
               "<tr><td style=\"padding:32px;color:#2c2c2c;font-size:15px;line-height:1.6;\">" +
               content +
               "</td></tr>" +
               "<tr><td style=\"background:#fff8f5;border-top:1px solid #ffe0d0;padding:16px 32px;text-align:center;color:#999;font-size:12px;\">" +
               "Equipo de People Care - Civica Software" +
               "</td></tr>" +
               "</table></td></tr></table></body></html>";
    }
}
