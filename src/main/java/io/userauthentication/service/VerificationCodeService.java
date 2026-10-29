package io.userauthentication.service;

import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.exception.NotImplementedException;
import io.lib.service.BaseJpaRepoEditService;
import io.lib.service.FormatUtil;
import io.lib.service.Message;
import io.notification.email.EmailService;
import io.notification.email.Outbox;
import io.notification.sms.SmsService;
import io.userauthentication.entity.VerificationCode;
import io.userauthentication.form.VerificationCodeRequestForm;
import io.userauthentication.repository.VerificationCodeRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class VerificationCodeService extends BaseJpaRepoEditService<VerificationCode, VerificationCodeRepository> {
    private final Integer MAX_CODE_COUNT = 3;
    private final Integer VERIFICATION_CODE_LEN = 5;
    private final Integer CODE_VALIDITY_IN_MINUTES = 15;
    private final Integer NUMBER_OF_MINUTES_BEFORE_RETRY = 15;

    private BCryptPasswordEncoder passwordEncoder;
    private SmsService smsService;
    private EmailService emailService;

    public VerificationCode generateVerificationCode(VerificationCodeRequestForm form){

        var optional = repository.findByUsernameTypeAndUsername(
                form.getUsernameType(),
                form.getUsername()
        );

        VerificationCode verificationCode = optional.orElseGet(VerificationCode::new);

        var timeBeforeResend =  verificationCode.getTimeLastSent().plusMinutes(
                NUMBER_OF_MINUTES_BEFORE_RETRY
        );

        if(verificationCode.getRetryCount() >= MAX_CODE_COUNT
                && timeBeforeResend.isAfter(LocalDateTime.now()) ){
            throw new CommonRuntimeException(
                ExceptionType.BAD_REQUEST,
                "error.verification.code.limit.exceeded"
            );
        }

        verificationCode.setUsernameType(form.getUsernameType());
        verificationCode.setVerificationCodeUse(form.getVerificationCodeUse());
        verificationCode.setUsername(form.getUsername());

        verificationCode.setRetryCount(verificationCode.getRetryCount() +1);

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(CODE_VALIDITY_IN_MINUTES);
        verificationCode.setExpiryTime(expiryTime);

        String code = RandomStringUtils.randomNumeric(VERIFICATION_CODE_LEN);
        verificationCode.setOtp(passwordEncoder.encode(code));
        verificationCode.setTimeLastSent(LocalDateTime.now());
        verificationCode = repository.save(verificationCode);

        sendCode(verificationCode, code);

        return verificationCode;
    }

    private void sendCode(VerificationCode verificationCode, String rawCode){
        switch (verificationCode.getUsernameType()){
            case PHONE_NUMBER -> sendOTPViaSms(verificationCode, rawCode);
            case EMAIL -> sendOTPViaEmail(verificationCode, rawCode);
            default -> throw new NotImplementedException();
        }
    }

    private void sendOTPViaSms(VerificationCode verificationCode, String rawCode){
        String msg = String.format(Message.get("verification.code.sms"), rawCode);
        smsService.send(verificationCode.getUsername(), msg);
    }

    private void sendOTPViaEmail(VerificationCode verificationCode, String rawCode){
        String msg = String.format(
                Message.get("verification.code.email.body"),
                rawCode,
                FormatUtil.getHumanReadableDateTime(verificationCode.getExpiryTime())
        );

        var outbox = new Outbox();
        outbox.setTo(Set.of(verificationCode.getUsername()));
        outbox.setSubject(Message.get("verification.code.email.subject"));
        outbox.setBodyHtml(msg);

        emailService.send(outbox);
    }

    public void verifyOtp(String entityId, String otp){
        VerificationCode verificationCode = findByEntityId(entityId);

        if(verificationCode.getExpiryTime().isBefore(LocalDateTime.now())){
            repository.delete(verificationCode);
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "error.verification.code.expired"
            );
        }

        boolean codeMatches = passwordEncoder.matches(
                otp,
                verificationCode.getOtp()
        );

        if(!codeMatches){
            throw new CommonRuntimeException(
                ExceptionType.BAD_REQUEST,
                    "invalid.verification.code"
            );
        }
    }

    public void deleteVerificationCode(String entityId){
        VerificationCode verificationCode = findByEntityId(entityId);
        repository.delete(verificationCode);
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
