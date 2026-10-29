package io.userauthentication.view;

import io.lib.view.BaseView;
import io.userauthentication.entity.VerificationCode;

import java.time.LocalDateTime;


public class VerificationCodeView extends BaseView<VerificationCode> {

    public VerificationCodeView(VerificationCode entity) {
        super(entity);
    }

    public LocalDateTime getExpiryTime(){
        return entity.getExpiryTime();
    }
}
