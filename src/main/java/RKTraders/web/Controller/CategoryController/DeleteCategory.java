package RKTraders.web.Controller.CategoryController;

import RKTraders.web.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class DeleteCategory {

    @Autowired
    CategoryService categoryService;

    @DeleteMapping("deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id){

        return categoryService.deleteCategory(id);

    }
}
