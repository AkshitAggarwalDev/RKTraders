package RKTraders.web.Service;

import RKTraders.web.Model.Admin;
import RKTraders.web.Model.Customer;
import RKTraders.web.Repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;


    public List<Admin> addAdmin(List<Admin> admins) {
        Admin user = new Admin();
        user.setRole("Admin");
        return adminRepo.saveAll(admins);
    }


    public List<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }


    public Optional<Admin> getAdminById(int id) {
        return adminRepo.findById(id);
    }


    public Optional<Admin> updateAdminById(int id, Admin updatedAdmin) {

        Optional<Admin> optionalAdmin = adminRepo.findById(id);

        if (optionalAdmin.isPresent()) {

            Admin existingAdmin = optionalAdmin.get();

            existingAdmin.setAdminName(updatedAdmin.getAdminName());
            existingAdmin.setAdminAge(updatedAdmin.getAdminAge());
            existingAdmin.setAdminSalary(updatedAdmin.getAdminSalary());
            existingAdmin.setAdminResidence(updatedAdmin.getAdminResidence());
            existingAdmin.setAdminPassword(updatedAdmin.getAdminPassword());

            return Optional.of(adminRepo.save(existingAdmin));
        }

        return Optional.empty();
    }


    public String deleteAdmin(int id) {

        if (adminRepo.existsById(id)) {

            adminRepo.deleteById(id);
            return "Admin Deleted Successfully";

        }

        return "Admin Not Found";
    }
}