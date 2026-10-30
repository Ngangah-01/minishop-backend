package io.productcatalog.form;

import io.lib.form.SessionUserIdForm;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryEditForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.product.category.name")
    private String name;

    @Size(max = 1028, message = "error.invalid.product.category.description.length")
    private String description;

    private String imageUrl;
}
