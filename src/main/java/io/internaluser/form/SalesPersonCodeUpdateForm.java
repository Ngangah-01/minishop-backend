package io.internaluser.form;

import io.lib.form.SessionUserIdForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesPersonCodeUpdateForm extends SessionUserIdForm {
    private String erpSalesPersonCode;
}