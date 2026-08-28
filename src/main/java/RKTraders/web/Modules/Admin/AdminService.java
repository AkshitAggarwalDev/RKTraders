package RKTraders.web.Modules.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private Repo adminRepo;


    public List<Entity> addAdmin(List<Entity> admins) {
        Entity user = new Entity();
        user.setRole("Admin");
        return adminRepo.saveAll(admins);
    }


    public List<Entity> getAllAdmins() {
        return adminRepo.findAll();
    }


    public Optional<Entity> getAdminById(int id) {
        return adminRepo.findById(id);
    }


    public Optional<Entity> updateAdminById(int id, Entity updatedAdmin) {

        Optional<Entity> optionalAdmin = adminRepo.findById(id);

        if (optionalAdmin.isPresent()) {

            Entity existingAdmin = optionalAdmin.get();

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