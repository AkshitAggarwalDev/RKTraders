package RKTraders.web.Modules.Owner;

import RKTraders.web.Modules.Admin.AdminEntity;
import RKTraders.web.Modules.Admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("owner")
public class OwnerAdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("addAdmins")
    public List<AdminEntity> addAdmin(@RequestBody List<AdminEntity> admins) {

        return adminService.addAdmin(admins);
    }


    @GetMapping("admins")
    public List<AdminEntity> getAllAdmins() {

        return adminService.getAllAdmins();
    }


    @GetMapping("admins/{id}")
    public Optional<AdminEntity> getAdminById(@PathVariable int id) {

        return adminService.getAdminById(id);
    }


    @PutMapping("admins/{id}")
    public Optional<AdminEntity> updateAdmin(@PathVariable int id,
                                             @RequestBody AdminEntity updatedAdmin) {

        return adminService.updateAdminById(id, updatedAdmin);
    }


    @DeleteMapping("admins/{id}")
    public String deleteAdmin(@PathVariable int id) {

        return adminService.deleteAdmin(id);
    }
}