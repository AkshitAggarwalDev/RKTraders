package RKTraders.web.Modules.Review;

import RKTraders.web.Modules.Customer.ReviewRequestDTO;
import RKTraders.web.Modules.Owner.ReviewSummaryDTO;
import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.DuplicateResourceException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    RKTraders.web.Modules.Customer.Repo customerRepo;
    @Autowired
    RKTraders.web.Modules.Product.Repo productRepo;
    @Autowired
    Repo reviewRepo;
    public String addReview(String email, Integer productId, ReviewRequestDTO reviewRequestDTO) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        RKTraders.web.Modules.Product.Entity product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Optional<Entity> existingReview =
                reviewRepo.findByCustomerIdAndProductId(
                        customer.getId(),
                        product.getId()
                );

        if (existingReview.isPresent()) {
            throw new DuplicateResourceException("You have already reviewed this product");
        }

        Entity review = new Entity();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(reviewRequestDTO.getRating());
        review.setComment(reviewRequestDTO.getComment());
        reviewRepo.save(review);
        return "Review Added Successfully";
    }

    public ReviewSummaryDTO getReviewSummary(Integer productId) {

        RKTraders.web.Modules.Product.Entity product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        List<Entity> reviews = reviewRepo.findByProductId(productId);

        ReviewSummaryDTO dto = new ReviewSummaryDTO();

        if (reviews.isEmpty()) {

            dto.setAverageRating(0.0);
            dto.setTotalReviews(0);

            dto.setFiveStar(0);
            dto.setFourStar(0);
            dto.setThreeStar(0);
            dto.setTwoStar(0);
            dto.setOneStar(0);

            return dto;
        }

        double totalRating = 0;

        for (Entity review : reviews) {

            totalRating += review.getRating();

            switch (review.getRating()) {

                case 5 -> dto.setFiveStar(dto.getFiveStar() + 1);

                case 4 -> dto.setFourStar(dto.getFourStar() + 1);

                case 3 -> dto.setThreeStar(dto.getThreeStar() + 1);

                case 2 -> dto.setTwoStar(dto.getTwoStar() + 1);

                case 1 -> dto.setOneStar(dto.getOneStar() + 1);

            }
        }

        dto.setTotalReviews(reviews.size());

        dto.setAverageRating(totalRating / reviews.size());

        return dto;
    }

    public List<Entity> getReviews(Integer productId) {

        RKTraders.web.Modules.Product.Entity product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        List<Entity> reviews = reviewRepo.findByProductId(productId);

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for this product");
        }

        return reviews;
    }

    public String deleteReview(String email, Integer reviewId) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        Entity review = reviewRepo.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found"));

        if (review.getCustomer().getId() != customer.getId()) {
            throw new BadRequestException("You can delete only your own review");
        }

        reviewRepo.delete(review);

        return "Review deleted successfully";
    }
    }