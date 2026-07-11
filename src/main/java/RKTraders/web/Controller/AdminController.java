package RKTraders.web.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {
    @GetMapping("dashboard")
    public String dashboardapi() {
        return null;
    }

    @PostMapping("addCategories")
    public String addCategory() {
        return null;
    }

    @GetMapping("allCategories")
    public String categories() {
        return null;
    }

    @PutMapping("updateCategory/{id}")
    public String updateCategory() {
        return null;
    }
    @DeleteMapping("deletCategory/{id}")
    public String deleteCategory() {
        return null;
    }

    @GetMapping("enquiries")
    public String allEnquiries() {
        return null;
    }

    @GetMapping("inventory")
    public String inventory() {
        return null;
    }

    @PatchMapping("product/{id}/stock")
    public String stock() {
        return null;
    }

    @GetMapping("customers")
    public String allCustomers() {
        return null;
    }


}
