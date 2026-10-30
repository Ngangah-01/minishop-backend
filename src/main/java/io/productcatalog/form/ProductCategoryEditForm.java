package io.productcatalog.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

@Getter
@Setter
public class ProductCategoryEditForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.product.category.name")
    private String name;

    @Size(max = 1028, message = "error.invalid.product.category.description.length")
    private String description;

    private String imageUrl;

    public String getName() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(name).trim());
    }

}
