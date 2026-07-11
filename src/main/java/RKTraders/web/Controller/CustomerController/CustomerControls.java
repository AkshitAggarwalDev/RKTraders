package RKTraders.web.Controller.CustomerController;

import RKTraders.web.DTO.PasswordUpdateDTO;
import RKTraders.web.Model.Customer;
import RKTraders.web.Model.Product;
import RKTraders.web.Service.CustomerService;
import RKTraders.web.Service.ProductService;
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
    public Customer getProfile(Authentication authentication) {

        return customerService.getProfile(authentication.getName());
        
    }

    @PutMapping("updateProfile")
    public  Customer updateProfile(Authentication authentication,
                                 @RequestBody Customer updatedCustomer) {

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
    public List<Product> getAllProducts(){

        return productService.getAllProducts();

    }

    @GetMapping("getProduct/{id}")
    public Product getProduct(@PathVariable int id){

        return productService.getProductById(id);

    }

    }
