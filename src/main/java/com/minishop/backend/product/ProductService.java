package com.minishop.backend.product;

import com.minishop.backend.category.Category;
import com.minishop.backend.category.CategoryRepository;
import com.minishop.backend.common.exception.ResourceNotFoundException;
import com.minishop.backend.product.dto.ProductRequest;
import com.minishop.backend.product.dto.ProductResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository; // injecting the repository to the service layer to access the database and perform CRUD operations on the Product entity.
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    //creating a product
    public ProductResponse createProduct(ProductRequest request){

        if(productRepository.existsBySku(request.sku())){
            throw new DataIntegrityViolationException("A Product with SKU  " + request.sku() + " already exists");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + request.categoryId() + " not found"));

        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                category,
                request.stockQuantity(),
                request.sku()
        );
        return ProductResponse.from(productRepository.save(product));
    }

    //updating a product
    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + request.categoryId() + " not found"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);
        product.setStockQuantity(request.stockQuantity());
        product.setSku(request.sku());

        Product updatedProduct = productRepository.save(product);
        return ProductResponse.from(updatedProduct);
    }

    //deleting a product
    public void deleteProduct(Long id){
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    //retrieving a product by id
    public ProductResponse findById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id
                ));
        return ProductResponse.from(product);
    }

    //retrieving all products
    public List<ProductResponse> findAll(){
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }
//    find by name
    public List <ProductResponse> findByName(String name){
        if(productRepository.findByNameContainingIgnoreCase(name).isEmpty()){
            throw new NoSuchElementException("No products found with name: " + name);
        }
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }

//    get products in a category
    public List<ProductResponse> findByCategoryId(Long categoryId){

//        checking if the category exists
        if(!categoryRepository.existsById(categoryId)){
            throw new ResourceNotFoundException("Category with id " + categoryId + " not found");
        }
//        checking if there are products in the category
        if(productRepository.findByCategoryId(categoryId).isEmpty()){
            throw new NoSuchElementException("No products found in category with id: " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }
}
