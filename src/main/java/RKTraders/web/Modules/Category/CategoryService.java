package RKTraders.web.Modules.Category;

import RKTraders.web.Exceptions.DuplicateResourceException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Modules.Product.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    RKTraders.web.Modules.Category.Repo categoryRepo;
    Entity category;
    Repo productRepo;

    public Entity addCategory(Entity category) {

        if (categoryRepo.existsByName(category.getName())) {
            return null;
        }

        return categoryRepo.save(category);
    }

    public Entity getCategoryById(int id){

        return categoryRepo.findById(id).orElse(null);

    }

    public Entity updateCategory(int id, Entity category){

        Optional<Entity> existing =
                categoryRepo.findByName(category.getName());

        if (existing.isPresent() &&
                existing.get().getId() != id) {

            throw new DuplicateResourceException("Category Already Exists");
        }

        Entity existingCategory = categoryRepo.findById(id).orElse(null);

        if(existingCategory != null){

            existingCategory.setName(category.getName());
            existingCategory.setDescription(category.getDescription());

            return categoryRepo.save(existingCategory);

        }

        return null;

    }

    public String deleteCategory(int id) {

        Optional<Entity> category = categoryRepo.findById(id);

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


    public Entity searchCategory(String name) {

        Optional<Entity> category =
                categoryRepo.findByNameIgnoreCase(name);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category Not Found");
        }

        return category.get();
    }


}
