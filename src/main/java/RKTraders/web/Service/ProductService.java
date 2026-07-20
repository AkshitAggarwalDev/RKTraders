package RKTraders.web.Service;

import RKTraders.web.Model.Category;
import RKTraders.web.Model.Product;
import RKTraders.web.Repositories.CategoryRepo;
import RKTraders.web.Repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
        @Autowired
        ProductRepo productRepo;
        CategoryRepo categoryRepo;



        public List<Product> addProducts(List<Product> products) {
            for (Product product : products) {
                product.setCreatedAt(LocalDateTime.now());
                product.setUpdatedAt(LocalDateTime.now());
            }

            return productRepo.saveAll(products);
        }



        public List<Product> getAllProducts() {
            return productRepo.findAll();
        }


        public Product getProductById(int id) {
            return productRepo.findById(id).orElse(null);
        }


        public Product updateProduct(int id, Product product) {

            Product existing = productRepo.findById(id).orElse(null);

            if(existing != null){

                existing.setName(product.getName());
                existing.setDescription(product.getDescription());
                existing.setPrice(product.getPrice());
                existing.setStock(product.getStock());
                existing.setBrand(product.getBrand());
                existing.setColor(product.getColor());
                existing.setCategory(product.getCategory());
                existing.setUpdatedAt(product.getUpdatedAt());

                return productRepo.save(existing);

            }

            return null;
        }



    public List<Product> getProductsByCategory(int categoryId) {

        Optional<Category> category =
                categoryRepo.findById(categoryId);

        if (category.isEmpty()) {
            throw new RuntimeException("Category Not Found");
        }

        return productRepo.findByCategory(category.get());
    }


    public long totalProductsInCategory(int categoryId) {

        Optional<Category> category = categoryRepo.findById(categoryId);

        if (category.isEmpty()) {
            throw new RuntimeException("Category Not Found");
        }

        return productRepo.countByCategory(category.get());
    }


        public String deleteProduct(int id) {

            productRepo.deleteById(id);

            return "Product Deleted Successfully";

        }

    public List<Product> searchProducts(String name) {

        List<Product> products = productRepo.findByNameContainingIgnoreCase(name);

        if (products.isEmpty()) {
            throw new RuntimeException("No Product Found");
        }

        return products;
    }

    public List<Product> getProductsByBrand(String brand) {

        List<Product> products = productRepo.findByBrandIgnoreCase(brand);

        if (products.isEmpty()) {
            throw new RuntimeException("No Products Found For This Brand");
        }

        return products;
    }

    public List<Product> getProductsByPriceRange(double minPrice,
                                                 double maxPrice) {

        List<Product> products =
                productRepo.findByPriceBetween(minPrice, maxPrice);

        if (products.isEmpty()) {
            throw new RuntimeException("No Products Found In This Price Range");
        }

        return products;
    }

    public List<Product> getOutOfStockProducts() {

        List<Product> products = productRepo.findByStock(0);

        if (products.isEmpty()) {
            throw new RuntimeException("No Out Of Stock Products");
        }

        return products;
    }

    public List<Product> sortByPriceAscending() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.ASC, "price")
        );
    }

    public List<Product> sortByPriceDescending() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.DESC, "price")
        );
    }

    public List<Product> sortByName() {

        return productRepo.findAll(
                Sort.by("name")
        );
    }

    public List<Product> latestProducts() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }
    }
