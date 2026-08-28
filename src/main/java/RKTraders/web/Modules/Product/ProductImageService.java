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



        @Value("${file.upload-dir}")
        private String uploadDir;

        public String uploadImage(int productId, MultipartFile image) throws IOException {

            ProductEntity product = productRepo.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

            Path path = Paths.get(uploadDir, fileName);

            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            ProductImageEntity productImage = new ProductImageEntity();

            productImage.setImageUrl(fileName);
            productImage.setPrimaryImage(false);
            productImage.setProduct(product);

            productImageRepo.save(productImage);

            return "Image Uploaded Successfully";
        }





    public List<ProductImageEntity> getImagesByProductId(int productId) {

        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<ProductImageEntity> images = productImageRepo.findByProductId(productId);

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images found for this product");
        }

        return images;
    }

    public String deleteImage(int imageId) throws IOException {

        ProductImageEntity image = productImageRepo.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        Path path = Paths.get("uploads", image.getImageUrl());

        Files.deleteIfExists(path);

        productImageRepo.delete(image);

        return "Image deleted successfully";
    }


    @Transactional
    public String setPrimaryImage(int imageId) {

        ProductImageEntity selectedImage = productImageRepo.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        int productId = selectedImage.getProduct().getId();

        List<ProductImageEntity> images = productImageRepo.findByProductId(productId);

        for(ProductImageEntity image : images){

            image.setPrimaryImage(false);

            productImageRepo.save(image);

        }

        selectedImage.setPrimaryImage(true);

        productImageRepo.save(selectedImage);

        return "Primary image updated successfully";
    }
    }