package RKTraders.web.Modules.Product;

import RKTraders.web.Modules.Owner.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class Controller {

    // Add Products :

    @Autowired
    ProductService productService;

    @PostMapping("addProducts")
    public List<Entity> addProducts(@RequestBody List<Entity> products) {

        return productService.addProducts(products);

    }

    // Get Products :


    @Autowired
    OwnerService ownerService;

    @GetMapping("allProducts")
    public List<Entity> getAllProducts() {

        return productService.getAllProducts();

    }

    @PutMapping("status/{productId}")
    public String changeStatus(
            @PathVariable int productId,
            @RequestParam ProductStatus status) {

        return ownerService.changeProductStatus(productId, status);
    }

    @GetMapping("getProduct/{id}")
    public Entity getProduct(@PathVariable int id) {

        return productService.getProductById(id);

    }


    @GetMapping("category/{categoryId}")
    public List<Entity> getProductsByCategory(
            @PathVariable int categoryId) {

        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("category/{categoryId}/totalProducts")
    public long totalProductsInCategory(
            @PathVariable int categoryId) {

        return productService.totalProductsInCategory(categoryId);
    }

    @GetMapping("search")
    public List<Entity> searchProducts(@RequestParam String name) {

        return productService.searchProducts(name);

    }

    @GetMapping("brand/{brand}")
    public List<Entity> getProductsByBrand(
            @PathVariable String brand) {

        return productService.getProductsByBrand(brand);
    }

    @GetMapping("price")
    public List<Entity> getProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {

        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("out-of-stock")
    public List<Entity> getOutOfStockProducts() {

        return productService.getOutOfStockProducts();
    }

    @GetMapping("sort/price/asc")
    public List<Entity> sortByPriceAscending() {

        return productService.sortByPriceAscending();
    }

    @GetMapping("sort/price/desc")
    public List<Entity> sortByPriceDescending() {

        return productService.sortByPriceDescending();
    }

    @GetMapping("sort/name")
    public List<Entity> sortByName() {

        return productService.sortByName();
    }

    @GetMapping("latest")
    public List<Entity> latestProducts() {

        return productService.latestProducts();
    }

    // Update Product :

    @PutMapping("updateProduct/{id}")
    public Entity updateProduct(@PathVariable int id,
                                @RequestBody Entity product) {

        return productService.updateProduct(id, product);

    }

    // Delete Product :

    @DeleteMapping("deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id) {

        return productService.deleteProduct(id);

    }
}

