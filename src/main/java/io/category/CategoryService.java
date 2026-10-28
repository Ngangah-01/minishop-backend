package io.category;

import io.category.dto.CategoryRequest;
import io.category.dto.CategoryResponse;
import io.common.exception.CategoryNotEmptyException;
import io.common.exception.ResourceNotFoundException;
import io.product.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

//    creating a new category
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByNameIgnoreCase(categoryRequest.name())) {
            throw new DataIntegrityViolationException("Category with name " + categoryRequest.name() + " already exists");
        }

        Category category = new Category(
                categoryRequest.name(),
                categoryRequest.description()
        );

        return CategoryResponse.from(categoryRepository.save(category));
    }

    // getting all categories
    public List<CategoryResponse> getAllCategories(){
        if(categoryRepository.findAll().isEmpty()){
            throw new ResourceNotFoundException("No categories found");
        }
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    // getting a category by id
    public CategoryResponse getCategoryById(Long id){
       Category category = categoryRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException(
                       "Category with id " + id + "not found"
               )
        );
       return CategoryResponse.from(category);
    }

    // updating a category
    public CategoryResponse updateCategory(Long id, CategoryRequest request)
    {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Category with id " + id + "not found"
                        )
                );
        if(categoryRepository.existsByNameIgnoreCase(request.name())
        && !category.getName().equalsIgnoreCase(request.name())){
            throw new IllegalArgumentException("Category with name " + request.name() + " already exists");
        };

        category.setName(request.name());
        category.setDescription(request.description());

//       Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.from(categoryRepository.save(category));
    }

    // deleting a category

    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotEmptyException(
                        "Category with id " + id + "not found"
                ));
        long productCount = productRepository.countByCategoryId(id);

        if(productCount > 0){
            throw new CategoryNotEmptyException(
                    "Cannot delete category with id " + id + " because it contains" + productCount + " product(s)"
            );
        }

        categoryRepository.delete(category);
    }
//    find by name
    public CategoryResponse getCategoryByName(String name){
        Category category = categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Category with name " + name + "not found"
                ));
        return CategoryResponse.from(category);
    }
}




