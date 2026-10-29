package io.customer.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerBankDetailsUpdateForm extends SessionUserIdForm {
    @NotBlank(message ="error.invalid.bank.id")
    private String bankId;

    @NotBlank(message ="error.invalid.account.number")
    private String bankAccountNumber;
}
