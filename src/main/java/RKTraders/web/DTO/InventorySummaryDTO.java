package RKTraders.web.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class InventorySummaryDTO {

        private long totalProducts;

        private long activeProducts;

        private long inactiveProducts;

        private long outOfStockProducts;

        private long lowStockProducts;
    }
