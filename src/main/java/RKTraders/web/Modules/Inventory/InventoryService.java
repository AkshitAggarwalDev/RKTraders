package RKTraders.web.Modules.Inventory;
import RKTraders.web.Modules.Owner.InventoryDTO;
import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Modules.Product.Entity;
import RKTraders.web.Modules.Product.Repo;
import RKTraders.web.Modules.Product.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    public class InventoryService {

        @Autowired
        private Repo productRepo;

        public List<Entity> getAllInventory() {

            List<Entity> products = productRepo.findAll();

            if (products.isEmpty()) {
                throw new ResourceNotFoundException("No products found in inventory");
            }

            return products;
        }


        public Entity getInventoryByProductId(Integer productId) {

            Entity product = productRepo.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            return product;
        }


        public Entity updateStock(Integer productId, Integer stock) {

            Entity product = productRepo.findById(productId)
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
        public Entity addStock(Integer productId, Integer quantity) {

            Entity product = productRepo.findById(productId)
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
        public Entity removeStock(Integer productId, Integer quantity) {

            Entity product = productRepo.findById(productId)
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
        public List<Entity> getLowStockProducts() {

            List<Entity> products = productRepo.findByStockLessThanEqual(10);

            if (products.isEmpty()) {
                throw new ResourceNotFoundException("No low stock products found");
            }

            return products;
        }

        // Get Out Of Stock Products
        public List<Entity> getOutOfStockProducts() {

            List<Entity> products = productRepo.findByStock(0);

            if (products.isEmpty()) {
                throw new ResourceNotFoundException("No out of stock products found");
            }

            return products;
        }

        // Inventory Summary
        public InventoryDTO getInventorySummary() {

            List<Entity> products = productRepo.findAll();

            InventoryDTO summary = new InventoryDTO();

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
