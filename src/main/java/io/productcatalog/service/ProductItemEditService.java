package io.productcatalog.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.service.BaseJpaRepoEditService;
import io.productcatalog.entity.ProductItem;
import io.productcatalog.form.ProductItemRegistrationForm;
import io.productcatalog.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductItemEditService extends BaseJpaRepoEditService<ProductItem, ProductItemRepository> {
    private ProductCategoryReadService productCategoryReadService;

    public ProductItem registerItem(ProductItemRegistrationForm form) {
        var productCategory = productCategoryReadService.findByEntityId(form.getProductCategoryEntityId());

        var  productItem = new ProductItem();
        productItem.setName(form.getName());
        productItem.setDescription(form.getDescription());
        productItem.setPrice(form.getPrice());
        productItem.setImageUrl(form.getImageUrl());
        productItem.setStatus(form.getStatus());
        productItem.setProductCategory(productCategory);
        productItem.setCreatedByEntityId(form.getSessionUserId());

        productItem = save(productItem, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(productItem.getEntityId());
        activityLogForm.setAction("Product item creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return productItem;
    }

    @Autowired
    public void setProductCategoryReadService(ProductCategoryReadService productCategoryReadService) {
        this.productCategoryReadService = productCategoryReadService;
    }
}
