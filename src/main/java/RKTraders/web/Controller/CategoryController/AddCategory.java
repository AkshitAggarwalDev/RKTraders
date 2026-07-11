package RKTraders.web.Controller.CategoryController;

import RKTraders.web.Model.Category;
import RKTraders.web.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("category")
public class AddCategory {

    @Autowired
    CategoryService categoryService;

    @PostMapping("addCategory")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {

        Category saved = categoryService.addCategory(category);

        if (saved == null) {
            return ResponseEntity.badRequest().body("Category Already Exists");
        }

        return ResponseEntity.ok(saved);
    }

    }
