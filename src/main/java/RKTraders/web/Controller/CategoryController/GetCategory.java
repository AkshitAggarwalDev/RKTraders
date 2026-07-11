package RKTraders.web.Controller.CategoryController;

import RKTraders.web.Model.Category;
import RKTraders.web.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("category")
public class GetCategory {

    @Autowired
    CategoryService categoryService;

    @GetMapping("getCategory/{id}")
    public Category getCategoryById(@PathVariable int id){

        return categoryService.getCategoryById(id);

    }

    @GetMapping("search")
    public Category searchCategory(
            @RequestParam String name) {

        return categoryService.searchCategory(name);
    }
}
