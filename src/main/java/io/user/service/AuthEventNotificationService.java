package io.user.service;

import io.httpaccesslog.service.HttpAccessLogService;
import io.lib.service.FormatUtil;
import io.lib.service.Message;
import io.notification.email.EmailService;
import io.notification.email.Outbox;
import io.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

@Service
class AuthEventNotificationService {
    private EmailService emailService;
    private HttpAccessLogService httpAccessLogService;

    void notifyOnLogin(User user, Locale locale, String httpAccessLogEntityId){
        var outbox = new Outbox();
        outbox.addTo(user.getEmail());
        outbox.setSubject(Message.get("new.login.email.subject", locale));

        var httpAccessLog = httpAccessLogService.findById(httpAccessLogEntityId);

        String htmlBody = String.format(
            Message.get("new.login.email.html.body", locale),
            user.getName(),
            FormatUtil.getHumanReadableDateTime(httpAccessLog.getCreatedAt()),
            httpAccessLog.getIpAddress()
        );
        outbox.setBodyHtml(htmlBody);

        String  plainTxtBody = String.format(
                Message.get("new.login.email.plain.body", locale),
                user.getName()
        );
        outbox.setBodyPlainText(plainTxtBody);

        emailService.send(outbox);
    }

    void notifyOnPasswordChange(User user){
        if(user.getEmail() == null){
            return;
        }

        Outbox outbox = new Outbox();
        outbox.setTo(Set.of(user.getEmail()));
        outbox.setSubject(Message.get("password.change.email.subject"));
        String body = String.format(
            Message.get("password.change.email.html.body"),
            user.getName(),
            FormatUtil.getHumanReadableDateTime(LocalDateTime.now())
        );
        outbox.setBodyHtml(body);

        emailService.send(outbox);
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setHttpAccessLogService(HttpAccessLogService httpAccessLogService) {
        this.httpAccessLogService = httpAccessLogService;
    }
}
