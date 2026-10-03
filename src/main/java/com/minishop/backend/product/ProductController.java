package com.minishop.backend.product;

import com.minishop.backend.common.response.ApiResponse;
import com.minishop.backend.product.dto.ProductRequest;
import com.minishop.backend.product.dto.ProductResponse;
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
