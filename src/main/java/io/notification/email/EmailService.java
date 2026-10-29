package io.notification.email;

public interface EmailService {
    /**
     * Sends out an email whose properties are defined in the outboxDto
     * @param outboxDTO {@link Outbox}. Holds email properties
     */
    void send(Outbox outboxDTO);
}
