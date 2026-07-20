package RKTraders.web.Controller.ProductImageController;

import RKTraders.web.Model.ProductImage;
import RKTraders.web.Service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product-images")
public class Upload {

        @Autowired
        private ProductImageService productImageService;

        @PostMapping("/upload/{productId}")
        public String uploadImage(
                @PathVariable int productId,
                @RequestParam("image") MultipartFile image) throws IOException {

            return productImageService.uploadImage(productId, image);
        }



    @RestController
    @RequestMapping("product-images")
    public class ProductImageController {

        @Autowired
        private ProductImageService productImageService;

        @GetMapping("/{productId}")
        public ResponseEntity<List<ProductImage>> getImages(
                @PathVariable int productId) {

            return ResponseEntity.ok(
                    productImageService.getImagesByProductId(productId)
            );
        }
    }



    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable int imageId) throws IOException {

        return ResponseEntity.ok(productImageService.deleteImage(imageId));

    }


    @PutMapping("primary/{imageId}")
    public ResponseEntity<String> setPrimaryImage(@PathVariable int imageId){

        return ResponseEntity.ok(productImageService.setPrimaryImage(imageId));

    }
    }
