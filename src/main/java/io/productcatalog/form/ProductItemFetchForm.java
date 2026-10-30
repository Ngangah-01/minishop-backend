package io.productcatalog.form;

import io.lib.form.BaseFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItemFetchForm extends BaseFetchForm {
    private String productCategoryId;
}
