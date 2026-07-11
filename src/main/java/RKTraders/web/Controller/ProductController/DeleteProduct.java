package RKTraders.web.Controller.ProductController;

import RKTraders.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Products")
public class DeleteProduct {
    @Autowired
    ProductService productService;
    @DeleteMapping("deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id){

        return productService.deleteProduct(id);

    }
}
