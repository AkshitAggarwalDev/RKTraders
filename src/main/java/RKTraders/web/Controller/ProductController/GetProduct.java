package RKTraders.web.Controller.ProductController;

import RKTraders.web.Model.Product;
import RKTraders.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("products")
public class GetProduct {

    @Autowired
    ProductService productService;
    @GetMapping("allProducts")
    public List<Product> getAllProducts(){

        return productService.getAllProducts();

    }

    @GetMapping("getProduct/{id}")
    public Product getProduct(@PathVariable int id){

        return productService.getProductById(id);

    }


    @GetMapping("category/{categoryId}")
    public List<Product> getProductsByCategory(
            @PathVariable int categoryId) {

        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("category/{categoryId}/totalProducts")
    public long totalProductsInCategory(
            @PathVariable int categoryId) {

        return productService.totalProductsInCategory(categoryId);
    }
}
