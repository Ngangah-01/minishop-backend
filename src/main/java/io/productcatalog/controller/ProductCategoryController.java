package io.productcatalog.controller;

import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.productcatalog.form.ProductCategoryRegistrationForm;
import io.productcatalog.service.ProductCategoryEditService;
import io.productcatalog.view.ProductCategoryView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/product-categories")
public class ProductCategoryController {

    private ProductCategoryEditService productCategoryEditService;

    @PreAuthorize("hasAuthority('REGISTER_PRODUCT_CATEGORY')")
    @PostMapping("register")
    public EntityApiResponse<ProductCategoryView> registerProductCategory(
            @RequestBody @Valid ProductCategoryRegistrationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var productCategory = productCategoryEditService.registerProductCategory(form);

        return new EntityApiResponse<>(
                Message.get("product.category.registration.success", locale),
                new ProductCategoryView(productCategory)
        );
    }

    @Autowired
    public void setProductCategoryEditService(ProductCategoryEditService service){
        this.productCategoryEditService = service;
    }

}
