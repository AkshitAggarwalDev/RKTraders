package RKTraders.web.Modules.Enquiry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnquiryService {

    @Autowired
    Repo enquiryRepo;

    public Entity addEnquiry(Entity enquiry){

        return enquiryRepo.save(enquiry);

    }

    public List<Entity> getAllEnquiries(){

        return enquiryRepo.findAll();

    }

    public Entity getEnquiryById(int id){

        return enquiryRepo.findById(id).orElse(null);

    }

    public Entity updateEnquiry(int id, Entity enquiry){

        Entity existingEnquiry = enquiryRepo.findById(id).orElse(null);

        if(existingEnquiry != null){

            existingEnquiry.setCustomerName(enquiry.getCustomerName());
            existingEnquiry.setEmail(enquiry.getEmail());
            existingEnquiry.setPhone(enquiry.getPhone());
            existingEnquiry.setMessage(enquiry.getMessage());
            existingEnquiry.setProduct(enquiry.getProduct());

            return enquiryRepo.save(existingEnquiry);

        }

        return null;

    }

    public String deleteEnquiry(int id){

        enquiryRepo.deleteById(id);

        return "Enquiry Deleted Successfully";

    }

}