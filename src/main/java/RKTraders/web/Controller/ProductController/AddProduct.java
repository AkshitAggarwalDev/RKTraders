package RKTraders.web.Controller.ProductController;

import RKTraders.web.Model.Product;
import RKTraders.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("products")
public class AddProduct {
    @Autowired
    ProductService productService;

    @PostMapping("addProducts")
    public List<Product> addProducts(@RequestBody List<Product> products) {

        return productService.addProducts(products);


    }
}
