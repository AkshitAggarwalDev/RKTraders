package RKTraders.web.Service;

import RKTraders.web.Model.Category;
import RKTraders.web.Model.Product;
import RKTraders.web.Repositories.CategoryRepo;
import RKTraders.web.Repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
        @Autowired
        ProductRepo productRepo;
        CategoryRepo categoryRepo;



        public List<Product> addProducts(List<Product> products) {
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
    }
