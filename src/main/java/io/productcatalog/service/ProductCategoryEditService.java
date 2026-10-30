package io.productcatalog.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;
import io.productcatalog.entity.ProductCategory;
import io.productcatalog.form.ProductCategoryEditForm;
import io.productcatalog.form.ProductCategoryRegistrationForm;
import io.productcatalog.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryEditService extends BaseJpaRepoEditService<ProductCategory, ProductCategoryRepository> {

    public ProductCategory registerProductCategory(ProductCategoryRegistrationForm form){
        var productCategory = new ProductCategory();
        mapCommonFields(productCategory, form);
        productCategory = save(productCategory, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(productCategory.getEntityId());
        activityLogForm.setAction("Product category registration");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return productCategory;
    }

    public ProductCategory editProductCategory(String productCategoryEntityId, ProductCategoryEditForm form){
        checkNameExists(productCategoryEntityId, form.getName());

        var productCategory = findByEntityId(productCategoryEntityId);
        mapCommonFields(productCategory, form);
        productCategory = save(productCategory, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(productCategory.getEntityId());
        activityLogForm.setAction("Product category edit");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return productCategory;
    }

    private void mapCommonFields(ProductCategory productCategory, ProductCategoryEditForm form){
        productCategory.setName(form.getName());
        productCategory.setDescription(form.getDescription());
        productCategory.setImageUrl(form.getImageUrl());
    }

    private void checkNameExists(String productCategoryEntityId, String name){
        var spec = repository.notDeleted()
                .and(repository.nameIs(name))
                .and(repository.entityIdNot(productCategoryEntityId));

        boolean exists = repository.exists(spec);

        if(exists){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.product.category.name");
        }
    }

}
