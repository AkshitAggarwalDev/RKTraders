package RKTraders.web.Modules.Category;

import RKTraders.web.Exceptions.DuplicateResourceException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Modules.Product.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;
    CategoryEntity category;
    ProductRepo productRepo;

    public CategoryEntity addCategory(CategoryEntity category) {

        if (categoryRepo.existsByName(category.getName())) {
            return null;
        }

        return categoryRepo.save(category);
    }

    public CategoryEntity getCategoryById(int id){

        return categoryRepo.findById(id).orElse(null);

    }

    public CategoryEntity updateCategory(int id, CategoryEntity category){

        Optional<CategoryEntity> existing =
                categoryRepo.findByName(category.getName());

        if (existing.isPresent() &&
                existing.get().getId() != id) {

            throw new DuplicateResourceException("Category Already Exists");
        }

        CategoryEntity existingCategory = categoryRepo.findById(id).orElse(null);

        if(existingCategory != null){

            existingCategory.setName(category.getName());
            existingCategory.setDescription(category.getDescription());

            return categoryRepo.save(existingCategory);

        }

        return null;

    }

    public String deleteCategory(int id) {

        Optional<CategoryEntity> category = categoryRepo.findById(id);

        if (category.isEmpty()) {
            return "Category Not Found";
        }

        long totalProducts = productRepo.countByCategory(category.get());

        if (totalProducts > 0) {
            return "Category Cannot Be Deleted. It Contains " + totalProducts + " Products";
        }

        categoryRepo.delete(category.get());

        return "Category Deleted Successfully";
    }


    public CategoryEntity searchCategory(String name) {

        Optional<CategoryEntity> category =
                categoryRepo.findByNameIgnoreCase(name);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category Not Found");
        }

        return category.get();
    }


}
