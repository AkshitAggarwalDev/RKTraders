package RKTraders.web.Controller.ProductController;

import RKTraders.web.Model.Product;
import RKTraders.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Products")
public class UpdateProduct {
    @Autowired
    ProductService productService;
    @PutMapping("updateProduct/{id}")
    public Product updateProduct(@PathVariable int id,
                                 @RequestBody Product product){

        return productService.updateProduct(id,product);

    }
}
