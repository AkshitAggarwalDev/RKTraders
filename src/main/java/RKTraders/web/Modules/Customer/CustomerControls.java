package RKTraders.web.Modules.Customer;

import RKTraders.web.Modules.Product.Entity;
import RKTraders.web.Modules.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerControls {
    @Autowired
    CustomerService customerService;

    @GetMapping("profile")
    public RKTraders.web.Modules.Customer.Entity getProfile(Authentication authentication) {

        return customerService.getProfile(authentication.getName());
        
    }

    @PutMapping("updateProfile")
    public RKTraders.web.Modules.Customer.Entity updateProfile(Authentication authentication,
                                                               @RequestBody RKTraders.web.Modules.Customer.Entity updatedCustomer) {

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
    public List<Entity> getAllProducts(){

        return productService.getAllProducts();

    }

    @GetMapping("getProduct/{id}")
    public Entity getProduct(@PathVariable int id){

        return productService.getProductById(id);

    }

    }
