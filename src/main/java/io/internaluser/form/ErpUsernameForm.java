package io.internaluser.form;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErpUsernameForm {
    //This is the identifier of a user on the ERP system, on the 'Portal Users' table.
    private String erpPortalUsername;

    //This is the identifier of a user on the ERP system, on the 'Salesperson/Purchaser' table
    private String erpSalesPersonCode;

    private String sessionUserId;
}
