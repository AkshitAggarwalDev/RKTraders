package RKTraders.web.Controller.ProductController;

import RKTraders.web.Model.Product;
import RKTraders.web.Service.OwnerService;
import RKTraders.web.Service.ProductService;
import RKTraders.web.enums.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("products")
public class GetProduct {

    @Autowired
    ProductService productService;

    @Autowired
    OwnerService ownerService;
    @GetMapping("allProducts")
    public List<Product> getAllProducts(){

        return productService.getAllProducts();

    }

    @PutMapping("status/{productId}")
    public String changeStatus(
            @PathVariable int productId,
            @RequestParam ProductStatus status) {

        return ownerService.changeProductStatus(productId, status);
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

    @GetMapping("search")
    public List<Product> searchProducts(@RequestParam String name) {

        return productService.searchProducts(name);

    }

    @GetMapping("brand/{brand}")
    public List<Product> getProductsByBrand(
            @PathVariable String brand) {

        return productService.getProductsByBrand(brand);
    }

    @GetMapping("price")
    public List<Product> getProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {

        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("out-of-stock")
    public List<Product> getOutOfStockProducts() {

        return productService.getOutOfStockProducts();
    }

    @GetMapping("sort/price/asc")
    public List<Product> sortByPriceAscending() {

        return productService.sortByPriceAscending();
    }

    @GetMapping("sort/price/desc")
    public List<Product> sortByPriceDescending() {

        return productService.sortByPriceDescending();
    }

    @GetMapping("sort/name")
    public List<Product> sortByName() {

        return productService.sortByName();
    }

    @GetMapping("latest")
    public List<Product> latestProducts() {

        return productService.latestProducts();
    }
}
