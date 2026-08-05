package RKTraders.web.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReviewSummaryDTO {

        private Double averageRating;

        private Integer totalReviews;

        private Integer fiveStar;

        private Integer fourStar;

        private Integer threeStar;

        private Integer twoStar;

        private Integer oneStar;

    }
