package io.productcatalog.form;

import io.lib.form.SessionUserIdForm;
import io.lib.service.Message;
import io.productcatalog.entity.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductItemEditForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.name")
    @Size(max = 255)
    private String name;

    @Size(max = 1028)
    @NotBlank(message = "error.invalid.description")
    private String description;

    private String imageUrl;

    @NotNull
    private ProductStatus status;

    private BigDecimal price;

    @NotBlank(message = "error.invalid.product.category")
    private String productCategoryEntityId;

}
