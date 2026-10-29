package io.userauthentication.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
public class CompleteLoginForm extends OtpForm{

    private String httpAccessLogEntityId;

    private Locale locale;
}
