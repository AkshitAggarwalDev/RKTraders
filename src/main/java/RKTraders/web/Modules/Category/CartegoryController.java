package RKTraders.web.Modules.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("category")
public class CartegoryController {


// Category Controller (Add Category) :

        @Autowired
        CategoryService categoryService;

        @PostMapping("addCategory")
        public ResponseEntity<?> addCategory(@RequestBody CategoryEntity category) {

            CategoryEntity saved = categoryService.addCategory(category);

            if (saved == null) {
                return ResponseEntity.badRequest().body("Category Already Exists");
            }

            return ResponseEntity.ok(saved);
        }

        // Get Category :

        @GetMapping("getCategory/{id}")
        public CategoryEntity getCategoryById(@PathVariable int id) {

            return categoryService.getCategoryById(id);

        }

        @GetMapping("search")
        public CategoryEntity searchCategory(
                @RequestParam String name) {

            return categoryService.searchCategory(name);
        }

        // Delete Category :

        @DeleteMapping("deleteCategory/{id}")
        public String deleteCategory(@PathVariable int id) {

            return categoryService.deleteCategory(id);

        }

        //Update Category :

        @PutMapping("updateCategory/{id}")
        public CategoryEntity updateCategory(@PathVariable int id,
                                             @RequestBody CategoryEntity category) {

            return categoryService.updateCategory(id, category);

        }
    }
