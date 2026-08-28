package RKTraders.web.Modules.Review;

import RKTraders.web.Modules.Customer.ReviewRequestDTO;
import RKTraders.web.Modules.Owner.ReviewSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/addReview/{productId}")
    public String addReview(Authentication authentication,
                            @PathVariable Integer productId,
                            @RequestBody ReviewRequestDTO reviewRequestDTO) {

        return reviewService.addReview(
                authentication.getName(),
                productId,
                reviewRequestDTO
        );
    }

    @GetMapping("summary/{productId}")
    public ResponseEntity<ReviewSummaryDTO> getReviewSummary(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                reviewService.getReviewSummary(productId)
        );
    }

    @GetMapping("getReview/{productId}")
    public ResponseEntity<List<ReviewEntity>> getReviews(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                reviewService.getReviews(productId)
        );
    }

    @DeleteMapping("deleteReview/{reviewId}")
    public ResponseEntity<String> deleteReview(
            Authentication authentication,
            @PathVariable Integer reviewId) {

        return ResponseEntity.ok(
                reviewService.deleteReview(
                        authentication.getName(),
                        reviewId
                )
        );
    }


}

