package RKTraders.web.Modules.Product;

import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
        @Autowired
        Repo productRepo;
        CategoryRepo categoryRepo;



        public List<Entity> addProducts(List<Entity> products) {
            for (Entity product : products) {
                product.setCreatedAt(LocalDateTime.now());
                product.setUpdatedAt(LocalDateTime.now());
            }

            return productRepo.saveAll(products);
        }



        public List<Entity> getAllProducts() {
            return productRepo.findAll();
        }


        public Entity getProductById(int id) {
            return productRepo.findById(id).orElse(null);
        }


        public Entity updateProduct(int id, Entity product) {

            Entity existing = productRepo.findById(id).orElse(null);

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



    public List<Entity> getProductsByCategory(int categoryId) {

        Optional<Category> category =
                categoryRepo.findById(categoryId);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category Not Found");
        }

        return productRepo.findByCategory(category.get());
    }


    public long totalProductsInCategory(int categoryId) {

        Optional<Category> category = categoryRepo.findById(categoryId);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category Not Found");
        }

        return productRepo.countByCategory(category.get());
    }


        public String deleteProduct(int id) {

            productRepo.deleteById(id);

            return "Product Deleted Successfully";

        }

    public List<Entity> searchProducts(String name) {

        List<Entity> products = productRepo.findByNameContainingIgnoreCase(name);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No Product Found");
        }

        return products;
    }

    public List<Entity> getProductsByBrand(String brand) {

        List<Entity> products = productRepo.findByBrandIgnoreCase(brand);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No Products Found For This Brand");
        }

        return products;
    }

    public List<Entity> getProductsByPriceRange(double minPrice,
                                                double maxPrice) {

        List<Entity> products =
                productRepo.findByPriceBetween(minPrice, maxPrice);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No Products Found In This Price Range");
        }

        return products;
    }

    public List<Entity> getOutOfStockProducts() {

        List<Entity> products = productRepo.findByStock(0);

        if (products.isEmpty()) {
            throw new BadRequestException("No Out Of Stock Products");
        }

        return products;
    }

    public List<Entity> sortByPriceAscending() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.ASC, "price")
        );
    }

    public List<Entity> sortByPriceDescending() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.DESC, "price")
        );
    }

    public List<Entity> sortByName() {

        return productRepo.findAll(
                Sort.by("name")
        );
    }

    public List<Entity> latestProducts() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }
    }
