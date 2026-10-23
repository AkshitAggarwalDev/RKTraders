package RKTraders.web.Modules.Product;

import RKTraders.web.Exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageService {

    @Autowired
    ProductImageRepo productImageRepo;

    @Autowired
    ProductRepo productRepo;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${CLOUDINARY_URL:}")
    private String cloudinaryUrl;

    private com.cloudinary.Cloudinary getCloudinary() {
        if (cloudinaryUrl == null || cloudinaryUrl.isBlank()) {
            throw new IllegalStateException("CLOUDINARY_URL is not configured");
        }

        return new com.cloudinary.Cloudinary(cloudinaryUrl);
    }

    public String uploadImage(int productId, MultipartFile image)
            throws IOException {

        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product Not Found"));

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Please select an image");
        }

        String publicId = "rk-traders/products/" + UUID.randomUUID();

        java.util.Map<?, ?> result = getCloudinary().uploader().upload(
                image.getBytes(),
                com.cloudinary.utils.ObjectUtils.asMap(
                        "public_id", publicId,
                        "resource_type", "image"
                )
        );

        String imageUrl = (String) result.get("secure_url");

        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IOException("Cloudinary image upload failed");
        }

        ProductImageEntity productImage = new ProductImageEntity();

        productImage.setImageUrl(imageUrl);
        productImage.setPrimaryImage(false);
        productImage.setProduct(product);

        productImageRepo.save(productImage);

        return "Image Uploaded Successfully";
    }

    public List<ProductImageEntity> getImagesByProductId(int productId) {

        productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        List<ProductImageEntity> images =
                productImageRepo.findByProductId(productId);

        if (images.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No images found for this product");
        }

        return images;
    }

    public String deleteImage(int imageId) throws IOException {

        ProductImageEntity image = productImageRepo.findById(imageId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Image not found"));

        String imageUrl = image.getImageUrl();

        if (imageUrl != null
                && imageUrl.startsWith("https://res.cloudinary.com/")) {

            java.net.URI uri = java.net.URI.create(imageUrl);
            String path = uri.getPath();
            String marker = "/image/upload/";
            int markerIndex = path.indexOf(marker);

            if (markerIndex < 0) {
                throw new IllegalArgumentException(
                        "Invalid Cloudinary image URL");
            }

            String publicId = path.substring(
                    markerIndex + marker.length());

            publicId = publicId.replaceFirst("^v\\d+/", "");

            int lastSlash = publicId.lastIndexOf('/');
            int lastDot = publicId.lastIndexOf('.');

            if (lastDot > lastSlash) {
                publicId = publicId.substring(0, lastDot);
            }

            getCloudinary().uploader().destroy(
                    publicId,
                    com.cloudinary.utils.ObjectUtils.asMap(
                            "resource_type", "image"
                    )
            );

        } else if (imageUrl != null && !imageUrl.isBlank()) {

            java.nio.file.Path path =
                    java.nio.file.Paths.get(uploadDir, imageUrl);

            java.nio.file.Files.deleteIfExists(path);
        }

        productImageRepo.delete(image);

        return "Image deleted successfully";
    }

    @Transactional
    public String setPrimaryImage(int imageId) {

        ProductImageEntity selectedImage =
                productImageRepo.findById(imageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Image not found"));

        int productId = selectedImage.getProduct().getId();

        List<ProductImageEntity> images =
                productImageRepo.findByProductId(productId);

        for (ProductImageEntity image : images) {
            image.setPrimaryImage(false);
            productImageRepo.save(image);
        }

        selectedImage.setPrimaryImage(true);
        productImageRepo.save(selectedImage);

        return "Primary image updated successfully";
    }
}