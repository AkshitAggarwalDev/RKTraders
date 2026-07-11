package RKTraders.web.Controller.OwnerController;

import RKTraders.web.Model.Admin;
import RKTraders.web.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("owner")
public class AdminManagement {

    @Autowired
    private AdminService adminService;


    @PostMapping("addAdmins")
    public List<Admin> addAdmin(@RequestBody List<Admin> admins) {

        return adminService.addAdmin(admins);
    }


    @GetMapping("admins")
    public List<Admin> getAllAdmins() {

        return adminService.getAllAdmins();
    }


    @GetMapping("admins/{id}")
    public Optional<Admin> getAdminById(@PathVariable int id) {

        return adminService.getAdminById(id);
    }


    @PutMapping("admins/{id}")
    public Optional<Admin> updateAdmin(@PathVariable int id,
                                       @RequestBody Admin updatedAdmin) {

        return adminService.updateAdminById(id, updatedAdmin);
    }


    @DeleteMapping("admins/{id}")
    public String deleteAdmin(@PathVariable int id) {

        return adminService.deleteAdmin(id);
    }
}