package RKTraders.web.Service;

import RKTraders.web.Exceptions.DuplicateResourceException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Model.Category;
import RKTraders.web.Repositories.CategoryRepo;
import RKTraders.web.Repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;
    Category category;
    ProductRepo productRepo;

    public Category addCategory(Category category) {

        if (categoryRepo.existsByName(category.getName())) {
            return null;
        }

        return categoryRepo.save(category);
    }

    public Category getCategoryById(int id){

        return categoryRepo.findById(id).orElse(null);

    }

    public Category updateCategory(int id, Category category){

        Optional<Category> existing =
                categoryRepo.findByName(category.getName());

        if (existing.isPresent() &&
                existing.get().getId() != id) {

            throw new DuplicateResourceException("Category Already Exists");
        }

        Category existingCategory = categoryRepo.findById(id).orElse(null);

        if(existingCategory != null){

            existingCategory.setName(category.getName());
            existingCategory.setDescription(category.getDescription());

            return categoryRepo.save(existingCategory);

        }

        return null;

    }

    public String deleteCategory(int id) {

        Optional<Category> category = categoryRepo.findById(id);

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


    public Category searchCategory(String name) {

        Optional<Category> category =
                categoryRepo.findByNameIgnoreCase(name);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category Not Found");
        }

        return category.get();
    }

}