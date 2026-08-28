package RKTraders.web.Modules.Owner;

import RKTraders.web.Modules.Customer.PasswordUpdateDTO;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Modules.Product.ProductEntity;

import RKTraders.web.Modules.Product.ProductRepo;
import RKTraders.web.Modules.Security.JwtService;
import RKTraders.web.Modules.Product.ProductStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OwnerRepo ownerRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Value("${owner.ownerName}")
    private String ownerName;


    @Value("${owner.ownerEmail}")
    private String ownerEmail;

    @Value("${owner.ownerPassword}")
    private String ownerPassword;

    @PostConstruct
    public void CreateDefaultOwner() {

        if (ownerRepo.count() == 0) {
            OwnerEntity owner = new OwnerEntity();
            owner.setOwnerName(ownerName);
            owner.setOwnerEmail(ownerEmail);
            owner.setOwnerPassword((encoder.encode(ownerPassword)));
            owner.setRole("OWNER");
            ownerRepo.save(owner);
            System.out.println("Default Owner Created !");
        }
    }


    public String loginOwner(OwnerLoginDTO loginRequest) {

        Optional<OwnerEntity> owner =
                ownerRepo.findByOwnerEmail(loginRequest.getOwnerEmail());

        System.out.println("Owner Found : " + owner.isPresent());

        if (owner.isEmpty()) {
            return "Owner Not Found";
        }

        OwnerEntity existingOwner = owner.get();

        System.out.println("DB Password : " + existingOwner.getOwnerPassword());

        boolean matched = encoder.matches(
                loginRequest.getOwnerPassword(),
                existingOwner.getOwnerPassword());

        System.out.println("Password Match : " + matched);

        if (matched) {

            System.out.println("Generating JWT...");

            return jwtService.generateToken(existingOwner.getOwnerEmail());
        }

        return "Invalid Password";
    }


    public OwnerEntity getProfile(String email) {

        return ownerRepo.findByOwnerEmail(email).orElse(null);

    }

    public OwnerEntity updateProfile(String email, OwnerEntity updatedOwner) {

        Optional<OwnerEntity> owner = ownerRepo.findByOwnerEmail(email);

        if (owner.isEmpty()) {
            return null;
        }

        OwnerEntity existingOwner = owner.get();

        existingOwner.setOwnerName(updatedOwner.getOwnerName());
        existingOwner.setOwnerEmail(updatedOwner.getOwnerEmail());
        existingOwner.setOwnerMobileNo(updatedOwner.getOwnerMobileNo());
        existingOwner.setCompanyName(updatedOwner.getCompanyName());
        existingOwner.setCompanyAddress(updatedOwner.getCompanyAddress());
        existingOwner.setGstNumber(updatedOwner.getGstNumber());

        return ownerRepo.save(existingOwner);
    }

    public String updatePassword(String email,
                                 PasswordUpdateDTO request) {

        Optional<OwnerEntity> owner = ownerRepo.findByOwnerEmail(email);

        if (owner.isEmpty()) {
            return "Owner Not Found";
        }

        OwnerEntity existingOwner = owner.get();

        if (!encoder.matches(request.getOldPassword(),
                existingOwner.getOwnerPassword())) {

            return "Old Password is Incorrect";
        }

        existingOwner.setOwnerPassword(
                encoder.encode(request.getNewPassword())
        );

        ownerRepo.save(existingOwner);

        return "Password Updated Successfully";

    }

    public String updateMobile(String email,
                               String mobileNo) {

        Optional<OwnerEntity> owner = ownerRepo.findByOwnerEmail(email);

        if (owner.isEmpty()) {
            return "Owner Not Found";
        }

        OwnerEntity existingOwner = owner.get();

        existingOwner.setOwnerMobileNo(mobileNo);

        ownerRepo.save(existingOwner);

        return "Mobile Number Updated Successfully";

    }

    @Autowired
    ProductRepo productRepo;

    public String changeProductStatus(int productId, ProductStatus status) {
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setStatus(status);

        productRepo.save(product);

        return "Product status updated successfully";


    }
}