package io.userauthentication.controller;

import io.httpaccesslog.entity.HttpAccessLog;
import io.lib.service.Message;
import io.lib.view.ApiResponse;
import io.lib.view.EntityApiResponse;
import io.user.entity.User;
import io.user.form.ChangePasswordForm;
import io.user.service.UserAuthService;
import io.userauthentication.form.CompleteLoginForm;
import io.userauthentication.form.InitLoginForm;
import io.userauthentication.form.NotificationIdUpdateForm;
import io.userauthentication.form.PasswordResetForm;
import io.userauthentication.validator.VerificationCodeExists;
import io.userauthentication.view.AuthView;
import io.userauthentication.view.VerificationCodeView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Locale;



@Validated
public abstract class AbstractAuthController<U extends User> {
    protected abstract UserAuthService<U, ?> getAuthService();

    @PostMapping("init-login")
    public EntityApiResponse<VerificationCodeView> initLogin(@RequestBody @Valid InitLoginForm form, Locale locale){
        var verificationCode = getAuthService().initLogin(form);

        var template = switch (form.getUsernameType()) {
            case PHONE_NUMBER -> "verification.code.sent.to.phone";
            case EMAIL -> "verification.code.sent.to.email";
        };

        var msg = String.format(
            Message.get(template, locale),
            form.getAnonymisedUsername()
        );

        return new EntityApiResponse<>(
            msg,
            new VerificationCodeView(verificationCode)
        );
    }

    @PostMapping("complete-login/{verificationCodeId}")
    public EntityApiResponse<AuthView> completeLogin(
            @PathVariable
            @VerificationCodeExists(message = "error.entity.not.found")
            String verificationCodeId,
            @RequestBody
            @Valid
            CompleteLoginForm form,
            Locale locale,
            HttpServletRequest req){

        form.setHttpAccessLogEntityId((String) req.getAttribute(HttpAccessLog.LOG_ID));
        form.setLocale(locale);

        var authView = getAuthService().completeLogin(form, verificationCodeId);

        return new EntityApiResponse<>(
            Message.get("complete.login.success", locale),
            authView
        );
    }

    @PostMapping("reset-password/{verificationCodeId}")
    public EntityApiResponse<AuthView> resetPassword(
        @PathVariable
        @VerificationCodeExists(message = "error.entity.not.found")
        String verificationCodeId,
        @RequestBody
        @Valid PasswordResetForm form,
        Locale locale
    ){
        var authView = getAuthService().resetPassword(form, verificationCodeId);
        return new EntityApiResponse<>(
            Message.get("password.change.success", locale),
            authView
        );
    }

    @PostMapping("change-password")
    public ApiResponse changePassword(
        @RequestBody
        @Valid
        ChangePasswordForm form,
        Authentication auth,
        Locale locale){
        getAuthService().changePassword(form, auth.getName());
        return new ApiResponse(
            true,
            HttpStatus.OK.value(),
            Message.get("password.change.success", locale)
        );
    }

    @PostMapping("update-notification-id")
    public ApiResponse updateNotificationId(
        @RequestBody
        @Valid
        NotificationIdUpdateForm form,
        Authentication auth,
        Locale locale
    ){
        form.setCreatedById(auth.getName());
        getAuthService().updateNotificationId(form, auth.getName());
        return new ApiResponse(
            Message.get("notification.id.updated", locale)
        );
    }
}
