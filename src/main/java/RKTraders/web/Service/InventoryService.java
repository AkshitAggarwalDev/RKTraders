package RKTraders.web.Service;
import RKTraders.web.DTO.InventorySummaryDTO;
import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Model.Product;
import RKTraders.web.Repositories.ProductRepo;
import RKTraders.web.enums.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    public class InventoryService {

        @Autowired
        private ProductRepo productRepo;

        public List<Product> getAllInventory() {

            List<Product> products = productRepo.findAll();

            if (products.isEmpty()) {
                throw new ResourceNotFoundException("No products found in inventory");
            }

            return products;
        }


        public Product getInventoryByProductId(Integer productId) {

            Product product = productRepo.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            return product;
        }


        public Product updateStock(Integer productId, Integer stock) {

            Product product = productRepo.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            if (stock < 0) {
                throw new BadRequestException("Stock cannot be negative");
            }

            product.setStock(stock);

            if (stock == 0) {
                product.setStatus(ProductStatus.INACTIVE);
            } else {
                product.setStatus(ProductStatus.ACTIVE);
            }

            return productRepo.save(product);
        }

        // Add Stock
        public Product addStock(Integer productId, Integer quantity) {

            Product product = productRepo.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            if (quantity <= 0) {
                throw new BadRequestException("Quantity must be greater than zero");
            }

            product.setStock(product.getStock() + quantity);

            if (product.getStatus() == ProductStatus.INACTIVE) {
                product.setStatus(ProductStatus.ACTIVE);
            }

            return productRepo.save(product);
        }

        // Remove Stock
        public Product removeStock(Integer productId, Integer quantity) {

            Product product = productRepo.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            if (quantity <= 0) {
                throw new BadRequestException("Quantity must be greater than zero");
            }

            if (quantity > product.getStock()) {
                throw new BadRequestException("Insufficient stock");
            }

            product.setStock(product.getStock() - quantity);

            if (product.getStock() == 0) {
                product.setStatus(ProductStatus.INACTIVE);
            }

            return productRepo.save(product);
        }

        // Get Low Stock Products
        public List<Product> getLowStockProducts() {

            List<Product> products = productRepo.findByStockLessThanEqual(10);

            if (products.isEmpty()) {
                throw new ResourceNotFoundException("No low stock products found");
            }

            return products;
        }

        // Get Out Of Stock Products
        public List<Product> getOutOfStockProducts() {

            List<Product> products = productRepo.findByStock(0);

            if (products.isEmpty()) {
                throw new ResourceNotFoundException("No out of stock products found");
            }

            return products;
        }

        // Inventory Summary
        public InventorySummaryDTO getInventorySummary() {

            List<Product> products = productRepo.findAll();

            InventorySummaryDTO summary = new InventorySummaryDTO();

            summary.setTotalProducts(products.size());

            long activeProducts = products.stream()
                    .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
                    .count();

            long inactiveProducts = products.stream()
                    .filter(product -> product.getStatus() == ProductStatus.INACTIVE)
                    .count();

            long outOfStockProducts = products.stream()
                    .filter(product -> product.getStock() == 0)
                    .count();

            long lowStockProducts = products.stream()
                    .filter(product -> product.getStock() > 0 && product.getStock() <= 10)
                    .count();

            summary.setActiveProducts(activeProducts);
            summary.setInactiveProducts(inactiveProducts);
            summary.setOutOfStockProducts(outOfStockProducts);
            summary.setLowStockProducts(lowStockProducts);

            return summary;
        }

    }
