package io.userauthentication.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class VerificationCode extends BaseJpaEntity {
    @Enumerated(EnumType.STRING)
    private UsernameType usernameType;

    @Enumerated(EnumType.STRING)
    private VerificationCodeUse verificationCodeUse;

    @Column(unique = true)
    private String username;

    private String otp;
    private LocalDateTime expiryTime;
    private Integer retryCount = 0;
    private LocalDateTime timeLastSent = LocalDateTime.now();
}
