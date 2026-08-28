package RKTraders.web.Modules.Enquiry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnquiryService {

    @Autowired
    EnquiryRepo enquiryRepo;

    public EnquiryEntity addEnquiry(EnquiryEntity enquiry){

        return enquiryRepo.save(enquiry);

    }

    public List<EnquiryEntity> getAllEnquiries(){

        return enquiryRepo.findAll();

    }

    public EnquiryEntity getEnquiryById(int id){

        return enquiryRepo.findById(id).orElse(null);

    }

    public EnquiryEntity updateEnquiry(int id, EnquiryEntity enquiry){

        EnquiryEntity existingEnquiry = enquiryRepo.findById(id).orElse(null);

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