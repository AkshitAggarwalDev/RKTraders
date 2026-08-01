package RKTraders.web.Controller;
import RKTraders.web.DTO.InventorySummaryDTO;
import RKTraders.web.Model.Product;
import RKTraders.web.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("inventory")
    public class InventoryController {

        @Autowired
        private InventoryService inventoryService;

        // Get Complete Inventory
        @GetMapping("all")
        public ResponseEntity<List<Product>> getAllInventory() {

            return ResponseEntity.ok(
                    inventoryService.getAllInventory()
            );
        }

        // Get Inventory By Product Id
        @GetMapping("{productId}")
        public ResponseEntity<Product> getInventoryByProductId(
                @PathVariable Integer productId) {

            return ResponseEntity.ok(
                    inventoryService.getInventoryByProductId(productId)
            );
        }

        // Update Stock
        @PutMapping("update/{productId}")
        public ResponseEntity<Product> updateStock(
                @PathVariable Integer productId,
                @RequestParam Integer stock) {

            return ResponseEntity.ok(
                    inventoryService.updateStock(productId, stock)
            );
        }

        // Add Stock
        @PatchMapping("add/{productId}")
        public ResponseEntity<Product> addStock(
                @PathVariable Integer productId,
                @RequestParam Integer quantity) {

            return ResponseEntity.ok(
                    inventoryService.addStock(productId, quantity)
            );
        }

        // Remove Stock
        @PatchMapping("remove/{productId}")
        public ResponseEntity<Product> removeStock(
                @PathVariable Integer productId,
                @RequestParam Integer quantity) {

            return ResponseEntity.ok(
                    inventoryService.removeStock(productId, quantity)
            );
        }

        // Low Stock Products
        @GetMapping("low-stock")
        public ResponseEntity<List<Product>> getLowStockProducts() {

            return ResponseEntity.ok(
                    inventoryService.getLowStockProducts()
            );
        }

        // Out Of Stock Products
        @GetMapping("out-of-stock")
        public ResponseEntity<List<Product>> getOutOfStockProducts() {

            return ResponseEntity.ok(
                    inventoryService.getOutOfStockProducts()
            );
        }

        // Inventory Summary
        @GetMapping("summary")
        public ResponseEntity<InventorySummaryDTO> getInventorySummary() {

            return ResponseEntity.ok(
                    inventoryService.getInventorySummary()
            );
        }
    }
