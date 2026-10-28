package io.user.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

@Getter
@Setter
public class UserEditForm extends SessionUserIdForm {
    private static final int MAX_NAME_LEN = 100;
    @NotBlank(message = "error.invalid.name")
    private String name;

    @Email(message = "error.invalid.email")
    private String email;

    @NotBlank(message = "error.invalid.phone.number")
    private String phoneNumber;

    @NotBlank(message = "error.invalid.country.code")
    private String countryCode;

    @NotBlank(message = "error.invalid.phone.code")
    private String phoneCode;

    @NotBlank(message = "error.invalid.national.id")
    private String nationalIdNumber;

    public String getName() {
        if (name.length() > MAX_NAME_LEN){
            name = name.substring(0, MAX_NAME_LEN);
        }
        return WordUtils.capitalize(name.toLowerCase()).trim();
    }

    public String getCountryCode() {
        return countryCode.trim().toUpperCase();
    }

    public String getPhoneCode() {
        return phoneCode.trim();
    }

    public String getPhoneNumber(){
        return phoneNumber.trim();
    }

    public String getEmail(){
        return StringUtils.isBlank(email)
                ? null
                : email.trim().toLowerCase();
    }
}
