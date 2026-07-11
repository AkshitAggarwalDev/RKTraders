package RKTraders.web.Controller;

import RKTraders.web.Model.Enquiry;
import RKTraders.web.Service.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enquiries")
public class EnquiryController {

    @Autowired
    EnquiryService enquiryService;

    @PostMapping("/addEnquiry")
    public Enquiry addEnquiry(@RequestBody Enquiry enquiry){

        return enquiryService.addEnquiry(enquiry);

    }

    @GetMapping("/allEnquiries")
    public List<Enquiry> getAllEnquiries(){

        return enquiryService.getAllEnquiries();

    }

    @GetMapping("/getEnquiry/{id}")
    public Enquiry getEnquiryById(@PathVariable int id){

        return enquiryService.getEnquiryById(id);

    }

    @PutMapping("/updateEnquiry/{id}")
    public Enquiry updateEnquiry(@PathVariable int id,
                                 @RequestBody Enquiry enquiry){

        return enquiryService.updateEnquiry(id, enquiry);

    }

    @DeleteMapping("/deleteEnquiry/{id}")
    public String deleteEnquiry(@PathVariable int id){

        return enquiryService.deleteEnquiry(id);

    }

}
