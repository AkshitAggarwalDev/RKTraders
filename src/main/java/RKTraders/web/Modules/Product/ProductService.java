package RKTraders.web.Modules.Product;

import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Modules.Category.CategoryEntity;
import RKTraders.web.Modules.Category.CategoryRepo;
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



        public List<ProductEntity> addProducts(List<ProductEntity> products) {
            for (ProductEntity product : products) {
                product.setCreatedAt(LocalDateTime.now());
                product.setUpdatedAt(LocalDateTime.now());
            }

            return productRepo.saveAll(products);
        }



        public List<ProductEntity> getAllProducts() {
            return productRepo.findAll();
        }


        public ProductEntity getProductById(int id) {
            return productRepo.findById(id).orElse(null);
        }


        public ProductEntity updateProduct(int id, ProductEntity product) {

            ProductEntity existing = productRepo.findById(id).orElse(null);

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



    public List<ProductEntity> getProductsByCategory(int categoryId) {

        Optional<CategoryEntity> category =
                categoryRepo.findById(categoryId);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category Not Found");
        }

        return productRepo.findByCategory(category.get());
    }


    public long totalProductsInCategory(int categoryId) {

        Optional<CategoryEntity> category = categoryRepo.findById(categoryId);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category Not Found");
        }

        return productRepo.countByCategory(category.get());
    }


        public String deleteProduct(int id) {

            productRepo.deleteById(id);

            return "Product Deleted Successfully";

        }

    public List<ProductEntity> searchProducts(String name) {

        List<ProductEntity> products = productRepo.findByNameContainingIgnoreCase(name);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No Product Found");
        }

        return products;
    }

    public List<ProductEntity> getProductsByBrand(String brand) {

        List<ProductEntity> products = productRepo.findByBrandIgnoreCase(brand);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No Products Found For This Brand");
        }

        return products;
    }

    public List<ProductEntity> getProductsByPriceRange(double minPrice,
                                                       double maxPrice) {

        List<ProductEntity> products =
                productRepo.findByPriceBetween(minPrice, maxPrice);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No Products Found In This Price Range");
        }

        return products;
    }

    public List<ProductEntity> getOutOfStockProducts() {

        List<ProductEntity> products = productRepo.findByStock(0);

        if (products.isEmpty()) {
            throw new BadRequestException("No Out Of Stock Products");
        }

        return products;
    }

    public List<ProductEntity> sortByPriceAscending() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.ASC, "price")
        );
    }

    public List<ProductEntity> sortByPriceDescending() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.DESC, "price")
        );
    }

    public List<ProductEntity> sortByName() {

        return productRepo.findAll(
                Sort.by("name")
        );
    }

    public List<ProductEntity> latestProducts() {

        return productRepo.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }
    }
