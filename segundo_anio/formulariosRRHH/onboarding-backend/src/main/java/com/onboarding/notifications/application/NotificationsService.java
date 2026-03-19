package com.onboarding.notifications.application;

import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.notifications.domain.model.Notifications;
import com.onboarding.notifications.domain.outport.NotificationsRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
        String subject = "Bienvenido/a al proceso de incorporación - Acceso a tu formulario";
        String body = String.format("""
                Hola %s %s,

                Has sido dado de alta en nuestro proceso de incorporación.
                Por favor, accede al siguiente enlace para completar tu documentación:

                %s

                Este enlace es personal e intransferible. Caduca en 30 días.

                Un saludo,
                Equipo de Recursos Humanos
                """, candidate.getFirstName(), candidate.getLastName(), link);

        sendAndRecord(processId, candidate.getEmail(), subject, body, Notifications.NotificationType.ACCESS_LINK);
    }

    public void sendFormSubmitted(String processId, String candidateFullName) {
        String subject = "Nuevo formulario completado - " + candidateFullName;
        String body = String.format("""
                Se ha recibido la documentación de %s y está pendiente de revisión.

                Accede al panel de administración para revisar el expediente.
                """, candidateFullName);

        sendAndRecord(processId, adminEmail, subject, body, Notifications.NotificationType.FORM_SUBMITTED);
    }

    public void sendValidated(String processId, String candidateEmail, String candidateFullName) {
        String subject = "Tu documentación ha sido validada";
        String body = String.format("""
                Hola %s,

                Tu documentación ha sido revisada y validada correctamente por el equipo de RRHH.
                En breve recibirás información sobre los próximos pasos.

                Un saludo,
                Equipo de Recursos Humanos
                """, candidateFullName);

        sendAndRecord(processId, candidateEmail, subject, body, Notifications.NotificationType.VALIDATED);
    }

    public void sendRejected(String processId, String candidateEmail, String candidateFullName, String notes) {
        String subject = "Revisión de tu documentación - Acción requerida";
        String body = String.format("""
                Hola %s,

                Tras revisar tu documentación, hemos detectado algunos problemas que requieren tu atención:

                %s

                Por favor, contacta con RRHH para más información.

                Un saludo,
                Equipo de Recursos Humanos
                """, candidateFullName, notes != null ? notes : "Consulta con el equipo de RRHH.");

        sendAndRecord(processId, candidateEmail, subject, body, Notifications.NotificationType.REJECTED);
    }

    private void sendAndRecord(String processId, String to, String subject, String body,
                               Notifications.NotificationType type) {
        Notifications.NotificationStatus status = Notifications.NotificationStatus.SENT;
        String errorMessage = null;

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email enviado a {} - tipo: {}", to, type);
        } catch (Exception e) {
            log.error("Error enviando email a {} - tipo: {}: {}", to, type, e.getMessage());
            status = Notifications.NotificationStatus.FAILED;
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
}
