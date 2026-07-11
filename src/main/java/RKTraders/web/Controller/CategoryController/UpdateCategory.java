package RKTraders.web.Controller.CategoryController;

import RKTraders.web.Model.Category;
import RKTraders.web.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
public class UpdateCategory {

    @Autowired
    CategoryService categoryService;

    @PutMapping("updateCategory/{id}")
    public Category updateCategory(@PathVariable int id,
                                   @RequestBody Category category){

        return categoryService.updateCategory(id, category);

    }
}
