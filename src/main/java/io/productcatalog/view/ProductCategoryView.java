package io.productcatalog.view;

import io.lib.view.BaseView;
import io.productcatalog.entity.ProductCategory;

public class ProductCategoryView extends BaseView<ProductCategory> {

    public ProductCategoryView(ProductCategory entity) {
        super(entity);
    }

    public String getName(){ return entity.getName(); }

    public String getDescription(){ return entity.getDescription(); }

    public String getImageUrl(){ return entity.getImageUrl(); }

}
