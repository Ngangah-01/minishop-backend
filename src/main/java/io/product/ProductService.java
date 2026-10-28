package io.product;

import io.category.Category;
import io.category.CategoryRepository;
import io.common.exception.ResourceNotFoundException;
import io.product.dto.ProductRequest;
import io.product.dto.ProductResponse;
import io.product.dto.ProductVariantRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository; // injecting the repository to the service layer to access the database and perform CRUD operations on the Product entity.
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductVariantRepository productVariantRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
    }

    //creating a product
    @Transactional
    public ProductResponse createProduct(ProductRequest request){

        if(productRepository.existsBySku(request.sku())
                || productVariantRepository.existsBySku(request.sku())){
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
        addImages(product, request.imageUrls());
        return ProductResponse.from(productRepository.save(product));
    }

    //updating a product
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + request.categoryId() + " not found"));

        if (!request.sku().equals(product.getSku())
                && (productRepository.existsBySku(request.sku())
                    || productVariantRepository.existsBySku(request.sku()))) {
            throw new DataIntegrityViolationException("SKU " + request.sku() + " is already in use");
        }

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);
        product.setStockQuantity(request.stockQuantity());
        product.setSku(request.sku());
        if (!product.getVariants().isEmpty()) {
            product.synchronizeStockWithVariants();
        }
        if (request.imageUrls() != null) {
            product.clearImages();
            addImages(product, request.imageUrls());
        }

        Product updatedProduct = productRepository.save(product);
        return ProductResponse.from(updatedProduct);
    }

    //deleting a product
    @Transactional
    public void deleteProduct(Long id){
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    //retrieving a product by id
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id
                ));
        return ProductResponse.from(product);
    }

    //retrieving all products
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(){
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }
//    find by name
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

    @Transactional
    public ProductResponse addImage(Long productId, String url) {
        Product product = findProduct(productId);
        product.addImage(new ProductImage(url, product.getImages().size()));
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse deleteImage(Long productId, Long imageId) {
        Product product = findProduct(productId);
        ProductImage image = product.getImages().stream()
                .filter(candidate -> candidate.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image with id " + imageId + " not found for product " + productId));

        product.getImages().remove(image);
        for (int index = 0; index < product.getImages().size(); index++) {
            product.getImages().get(index).setDisplayOrder(index);
        }
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse addVariant(Long productId, ProductVariantRequest request) {
        Product product = findProduct(productId);
        validateVariant(request);
        ensureSkuAvailable(request.sku(), null);
        product.addVariant(new ProductVariant(
                request.size(), request.color(), request.sku(), request.stockQuantity()));
        product.synchronizeStockWithVariants();
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateVariant(Long productId, Long variantId, ProductVariantRequest request) {
        Product product = findProduct(productId);
        ProductVariant variant = findVariant(product, variantId);
        validateVariant(request);
        ensureSkuAvailable(request.sku(), variantId);
        variant.setSize(request.size());
        variant.setColor(request.color());
        variant.setSku(request.sku());
        variant.setStockQuantity(request.stockQuantity());
        product.synchronizeStockWithVariants();
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse deleteVariant(Long productId, Long variantId) {
        Product product = findProduct(productId);
        ProductVariant variant = findVariant(product, variantId);
        product.getVariants().remove(variant);
        product.synchronizeStockWithVariants();
        return ProductResponse.from(productRepository.save(product));
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + productId + " not found"));
    }

    private ProductVariant findVariant(Product product, Long variantId) {
        return product.getVariants().stream()
                .filter(variant -> variant.getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant with id " + variantId + " not found for product " + product.getId()));
    }

    private void addImages(Product product, List<String> imageUrls) {
        if (imageUrls == null) {
            return;
        }
        for (int index = 0; index < imageUrls.size(); index++) {
            product.addImage(new ProductImage(imageUrls.get(index), index));
        }
    }

    private void validateVariant(ProductVariantRequest request) {
        if ((request.size() == null || request.size().isBlank())
                && (request.color() == null || request.color().isBlank())) {
            throw new IllegalArgumentException("A variant must specify a size, a color, or both");
        }
    }

    private void ensureSkuAvailable(String sku, Long variantId) {
        if (productRepository.existsBySku(sku)
                || (variantId == null
                    ? productVariantRepository.existsBySku(sku)
                    : productVariantRepository.existsBySkuAndIdNot(sku, variantId))) {
            throw new DataIntegrityViolationException("SKU " + sku + " is already in use");
        }
    }
}
