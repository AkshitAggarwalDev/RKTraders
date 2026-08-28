package RKTraders.web.Modules.Customer;

import RKTraders.web.Modules.Product.ProductEntity;
import RKTraders.web.Modules.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("profile")
    public CustomerEntity getProfile(Authentication authentication) {

        return customerService.getProfile(authentication.getName());
        
    }

    @PutMapping("updateProfile")
    public CustomerEntity updateProfile(Authentication authentication,
                                        @RequestBody CustomerEntity updatedCustomer) {

        return customerService.updateProfile(
                authentication.getName(),
                updatedCustomer
        );
    }
    @PatchMapping("updatePassword")
    public String updatePassword(Authentication authentication,
                                 @RequestBody PasswordUpdateDTO request) {

        return customerService.updatePassword(
                authentication.getName(),
                request
        );
    }
    @PatchMapping("mobile")
    public String updateMobileNo(){
        return null;
    }

    @DeleteMapping("deleteProfile")
    public String deleteProfile(Authentication authentication) {

        return customerService.deleteProfile(
                authentication.getName()
        );
    }

    @Autowired
    ProductService productService;
    @GetMapping("allProducts")
    public List<ProductEntity> getAllProducts(){

        return productService.getAllProducts();

    }

    @GetMapping("getProduct/{id}")
    public ProductEntity getProduct(@PathVariable int id){

        return productService.getProductById(id);

    }

    }
