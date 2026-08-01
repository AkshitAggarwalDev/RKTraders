package RKTraders.web.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReviewSummaryDTO {

        private Double averageRating;

        private Long totalReviews;

        private Long fiveStar;

        private Long fourStar;

        private Long threeStar;

        private Long twoStar;

        private Long oneStar;

    }
