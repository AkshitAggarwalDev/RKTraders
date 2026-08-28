package RKTraders.web.Modules.Product;

import RKTraders.web.Modules.Owner.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    // Add Products :

    @Autowired
    ProductService productService;

    @PostMapping("addProducts")
    public List<ProductEntity> addProducts(@RequestBody List<ProductEntity> products) {

        return productService.addProducts(products);

    }

    // Get Products :


    @Autowired
    OwnerService ownerService;

    @GetMapping("allProducts")
    public List<ProductEntity> getAllProducts() {

        return productService.getAllProducts();

    }

    @PutMapping("status/{productId}")
    public String changeStatus(
            @PathVariable int productId,
            @RequestParam ProductStatus status) {

        return ownerService.changeProductStatus(productId, status);
    }

    @GetMapping("getProduct/{id}")
    public ProductEntity getProduct(@PathVariable int id) {

        return productService.getProductById(id);

    }


    @GetMapping("category/{categoryId}")
    public List<ProductEntity> getProductsByCategory(
            @PathVariable int categoryId) {

        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("category/{categoryId}/totalProducts")
    public long totalProductsInCategory(
            @PathVariable int categoryId) {

        return productService.totalProductsInCategory(categoryId);
    }

    @GetMapping("search")
    public List<ProductEntity> searchProducts(@RequestParam String name) {

        return productService.searchProducts(name);

    }

    @GetMapping("brand/{brand}")
    public List<ProductEntity> getProductsByBrand(
            @PathVariable String brand) {

        return productService.getProductsByBrand(brand);
    }

    @GetMapping("price")
    public List<ProductEntity> getProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {

        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("out-of-stock")
    public List<ProductEntity> getOutOfStockProducts() {

        return productService.getOutOfStockProducts();
    }

    @GetMapping("sort/price/asc")
    public List<ProductEntity> sortByPriceAscending() {

        return productService.sortByPriceAscending();
    }

    @GetMapping("sort/price/desc")
    public List<ProductEntity> sortByPriceDescending() {

        return productService.sortByPriceDescending();
    }

    @GetMapping("sort/name")
    public List<ProductEntity> sortByName() {

        return productService.sortByName();
    }

    @GetMapping("latest")
    public List<ProductEntity> latestProducts() {

        return productService.latestProducts();
    }

    // Update Product :

    @PutMapping("updateProduct/{id}")
    public ProductEntity updateProduct(@PathVariable int id,
                                       @RequestBody ProductEntity product) {

        return productService.updateProduct(id, product);

    }

    // Delete Product :

    @DeleteMapping("deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id) {

        return productService.deleteProduct(id);

    }
}

