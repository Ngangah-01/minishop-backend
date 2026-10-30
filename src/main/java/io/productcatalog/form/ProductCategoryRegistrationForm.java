package io.productcatalog.form;

import io.productcatalog.validator.UniqueProductCategoryName;

public class ProductCategoryRegistrationForm extends ProductCategoryEditForm{

    @UniqueProductCategoryName
    public String getName(){
        return super.getName();
    }
}
