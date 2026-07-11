package RKTraders.web.Service;


import RKTraders.web.Model.Enquiry;
import RKTraders.web.Repositories.EnquiryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnquiryService {

    @Autowired
    EnquiryRepo enquiryRepo;

    public Enquiry addEnquiry(Enquiry enquiry){

        return enquiryRepo.save(enquiry);

    }

    public List<Enquiry> getAllEnquiries(){

        return enquiryRepo.findAll();

    }

    public Enquiry getEnquiryById(int id){

        return enquiryRepo.findById(id).orElse(null);

    }

    public Enquiry updateEnquiry(int id, Enquiry enquiry){

        Enquiry existingEnquiry = enquiryRepo.findById(id).orElse(null);

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