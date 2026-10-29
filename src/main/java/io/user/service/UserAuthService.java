package io.user.service;

import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.exception.NotImplementedException;
import io.lib.service.BaseJpaRepoEditService;
import io.user.entity.User;
import io.user.entity.UserStatus;
import io.user.entity.UserType;
import io.user.form.ChangePasswordForm;
import io.user.repository.UserRepository;
import io.userauthentication.entity.UsernameType;
import io.userauthentication.entity.VerificationCode;
import io.userauthentication.entity.VerificationCodeUse;
import io.userauthentication.form.*;
import io.userauthentication.service.JwtService;
import io.userauthentication.service.OnUsernameVerificationListener;
import io.userauthentication.service.VerificationCodeService;
import io.userauthentication.view.AuthView;
import io.userauthentication.view.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class UserAuthService<U extends User, R extends UserRepository<U>> extends BaseJpaRepoEditService<U, R> {
    private VerificationCodeService verificationCodeService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    private AuthEventNotificationService authEventNotificationService;
    private List<OnUsernameVerificationListener> usernameVerificationListeners;

    public abstract UserType getUserType();

    public VerificationCode initLogin(InitLoginForm form) {
        U user = getUser(form);



        validateUserStatus(user);

        validatePassword(user, form);

        var codeRequestForm = new VerificationCodeRequestForm();
        codeRequestForm.setUsername(form.getUsername());
        codeRequestForm.setUsernameType(form.getUsernameType());
        codeRequestForm.setVerificationCodeUse(VerificationCodeUse.AUTHENTICATION);

        return verificationCodeService.generateVerificationCode(codeRequestForm);
    }

    private U getUser(InitLoginForm form){
        return switch (form.getUsernameType()) {
            case EMAIL -> getUserByEmail(form.getUsername());
            case PHONE_NUMBER -> getUserByPhoneNumber(form.getUsername());
        };
    }

    protected void validatePassword(U user, InitLoginForm form){
        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new CommonRuntimeException(
                    ExceptionType.UNAUTHORIZED,
                    "error.invalid.username.password"
            );
        }
    }

    public AuthView completeLogin(CompleteLoginForm form, String verificationCodeId) {
        VerificationCode verificationCode = verificationCodeService.findByEntityId(verificationCodeId);

        U user = switch (verificationCode.getUsernameType()) {
            case EMAIL -> getUserByEmail(verificationCode.getUsername());
            case PHONE_NUMBER -> getUserByPhoneNumber(verificationCode.getUsername());
        };

        verificationCodeService.verifyOtp(verificationCodeId, form.getOtp());
        verificationCodeService.deleteVerificationCode(verificationCodeId);

        usernameVerificationListeners.forEach(
                listener -> listener.onSuccessfulVerification(verificationCode.getUsernameType(), user.getEntityId())
        );

        return onUserAuthentication(user);
    }

    public AuthView resetPassword(PasswordResetForm form, String verificationCodeId) {
        VerificationCode verificationCode = verificationCodeService.findByEntityId(verificationCodeId);

        verificationCodeService.verifyOtp(verificationCodeId, form.getOtp());
        String username = verificationCode.getUsername();
        UsernameType usernameType = verificationCode.getUsernameType();

        U user = switch (usernameType) {
            case EMAIL -> getUserByEmail(username);
            case PHONE_NUMBER -> getUserByPhoneNumber(username);
        };

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new CommonRuntimeException(
                    ExceptionType.UNAUTHORIZED,
                    "error.user.not.active"
            );
        }

        user.setPassword(passwordEncoder.encode(form.getPassword()));

        switch (usernameType) {
            case PHONE_NUMBER -> user.setPhoneNumberVerified(true);
            case EMAIL -> user.setEmailVerified(true);
            default -> throw new NotImplementedException();
        }

        user = save(user);
        verificationCodeService.deleteVerificationCode(verificationCodeId);
        authEventNotificationService.notifyOnPasswordChange(user);

        return onUserAuthentication(user);
    }

    public void changePassword(ChangePasswordForm form, String entityId) {
        U user = findByEntityId(entityId);
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user = save(user);
        authEventNotificationService.notifyOnPasswordChange(user);
    }

    public U getUserByPhoneNumber(String phoneNumber) {
        Specification<U> spec = repository.notDeleted()
                .and(repository.phoneNumberIs(phoneNumber));

        Optional<U> userOpt = repository.findOne(spec);

        if (userOpt.isEmpty()) {
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "error.user.phone.number.not.found"
            );
        }
        return userOpt.get();
    }

    public U getUserByEmail(String email) {

        Specification<U> spec = repository.notDeleted()
                .and(repository.emailIs(email));

        Optional<U> userOpt = repository.findOne(spec);

        if (userOpt.isEmpty()) {
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "error.user.email.not.found"
            );
        }
        return userOpt.get();
    }

    public AuthView onUserAuthentication(U user) {
        Jwt jwt = jwtService.generateJwt(user);
        user.setRecentAuthId(passwordEncoder.encode(jwt.getJwtId()));
        save(user);

        return new AuthView(jwt);
    }

    public void updateNotificationId(NotificationIdUpdateForm form, String userEntityId) {
        U user = findByEntityId(userEntityId);
        user.setNotificationId(form.getNotificationId());
        save(user, form.getSessionUserId());
    }


    public U registerPubKey(PublicKeyForm form, String verificationCodeId) {
        VerificationCode verificationCode = verificationCodeService.findByEntityId(verificationCodeId);

        U user = switch (verificationCode.getUsernameType()) {
            case EMAIL -> getUserByEmail(verificationCode.getUsername());
            case PHONE_NUMBER -> getUserByPhoneNumber(verificationCode.getUsername());
        };

        verificationCodeService.verifyOtp(verificationCodeId, form.getOtp());
        verificationCodeService.deleteVerificationCode(verificationCodeId);

        user.setPublicKey(form.getPublicKey());
        user = save(user);

        return user;
    }

    public List<String> getUserPermissions(U user) {
        return List.of();
    }

    public void validateUserStatus(U user) {
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "error.user.not.active"
            );
        }
    }

    @Autowired
    public void setVerificationCodeService(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    void setAuthEventNotificationService(AuthEventNotificationService authEventNotificationService) {
        this.authEventNotificationService = authEventNotificationService;
    }

    @Autowired
    public void setUsernameVerificationListeners(List<OnUsernameVerificationListener> usernameVerificationListeners) {
        this.usernameVerificationListeners = usernameVerificationListeners;
    }
}
