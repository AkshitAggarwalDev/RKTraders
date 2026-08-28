package RKTraders.web.Modules.Owner;

import RKTraders.web.Modules.Admin.Entity;
import RKTraders.web.Modules.Admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("owner")
public class AdminCRUD {

    @Autowired
    private AdminService adminService;


    @PostMapping("addAdmins")
    public List<Entity> addAdmin(@RequestBody List<Entity> admins) {

        return adminService.addAdmin(admins);
    }


    @GetMapping("admins")
    public List<Entity> getAllAdmins() {

        return adminService.getAllAdmins();
    }


    @GetMapping("admins/{id}")
    public Optional<Entity> getAdminById(@PathVariable int id) {

        return adminService.getAdminById(id);
    }


    @PutMapping("admins/{id}")
    public Optional<Entity> updateAdmin(@PathVariable int id,
                                        @RequestBody Entity updatedAdmin) {

        return adminService.updateAdminById(id, updatedAdmin);
    }


    @DeleteMapping("admins/{id}")
    public String deleteAdmin(@PathVariable int id) {

        return adminService.deleteAdmin(id);
    }
}