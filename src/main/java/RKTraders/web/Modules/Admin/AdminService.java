package RKTraders.web.Modules.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;


    public List<AdminEntity> addAdmin(List<AdminEntity> admins) {
        AdminEntity user = new AdminEntity();
        user.setRole("Admin");
        return adminRepo.saveAll(admins);
    }


    public List<AdminEntity> getAllAdmins() {
        return adminRepo.findAll();
    }


    public Optional<AdminEntity> getAdminById(int id) {
        return adminRepo.findById(id);
    }


    public Optional<AdminEntity> updateAdminById(int id, AdminEntity updatedAdmin) {

        Optional<AdminEntity> optionalAdmin = adminRepo.findById(id);

        if (optionalAdmin.isPresent()) {

            AdminEntity existingAdmin = optionalAdmin.get();

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