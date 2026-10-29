package io.user.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class User extends BaseJpaEntity {

    @Column(length = 100)
    private String name;

    @Column(length = 5)
    private String countryCode;

    @Column(length = 4)
    private String phoneCode;

    @Column(unique = true, length = 50)
    private String phoneNumber;

    private boolean phoneNumberVerified;

    @Column(unique = true, length = 100)
    private String email;

    private boolean emailVerified;

    private String password;
    private String recentAuthId;
    private String notificationId;
    private String nationalIdNumber;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(length = 2048)
    private String publicKey;

    public abstract UserType getUserType();

    public abstract String getUserTypeStr();

    public String getUsername(){
        String email = getEmail() == null ? "" : getEmail();
        return String.format("%s | %s %s %s", getName(), email, phoneNumber, getUserTypeStr());
    }

    public boolean isActive(){
        return getUserStatus() == UserStatus.ACTIVE;
    }
}
