package io.product;

import io.common.response.ApiResponse;
import io.product.dto.ProductRequest;
import io.product.dto.ProductResponse;
import io.product.dto.ProductImageRequest;
import io.product.dto.ProductVariantRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

//    CREATE PRODUCT
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request
    ){
        ProductResponse createdProduct = productService.createProduct(request);
//        URI location = URI.create(String.format("/api/products/%s", createdProduct.id()));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", createdProduct));
    }

//    UPDATE PRODUCT
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ){
        ProductResponse updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Product updated successfully", updatedProduct)
        );
    }

    //DELETE PRODUCT
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ApiResponse.success("Product deleted successfully", null)
        );
    }

    //GET PRODUCT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id){
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Product retrieved successfully", product));
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ApiResponse<ProductResponse>> addProductImage(
            @PathVariable Long id,
            @Valid @RequestBody ProductImageRequest request
    ) {
        ProductResponse product = productService.addImage(id, request.url());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product image added successfully", product));
    }

    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<ApiResponse<ProductResponse>> deleteProductImage(
            @PathVariable Long id,
            @PathVariable Long imageId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Product image deleted successfully", productService.deleteImage(id, imageId)));
    }

    @PostMapping("/{id}/variants")
    public ResponseEntity<ApiResponse<ProductResponse>> addProductVariant(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        ProductResponse product = productService.addVariant(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product variant added successfully", product));
    }

    @PutMapping("/{id}/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductVariant(
            @PathVariable Long id,
            @PathVariable Long variantId,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Product variant updated successfully",
                productService.updateVariant(id, variantId, request)));
    }

    @DeleteMapping("/{id}/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductResponse>> deleteProductVariant(
            @PathVariable Long id,
            @PathVariable Long variantId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Product variant deleted successfully",
                productService.deleteVariant(id, variantId)));
    }

//    GET ALL PRODUCTS
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId
    ){
      if(name != null && !name.isBlank()){
          return ResponseEntity.ok(
                    ApiResponse.success("Products retrieved successfully", productService.findByName(name))
          );
      } else if(categoryId != null ){
          return ResponseEntity.ok(
                    ApiResponse.success("Products retrieved successfully", productService.findByCategoryId(categoryId))
          );
      } else {
          return ResponseEntity.ok(
                    ApiResponse.success("Products retrieved successfully", productService.findAll())
          );
      }
    }
}
