package io.productcatalog.form;

import io.lib.form.SessionUserIdForm;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryEditForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.product.category.name")
    private String name;

    @Column(length = 1028)
    private String description;

    private String imageUrl;
}
