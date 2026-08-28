package RKTraders.web.Modules.Enquiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enquiries")
public class EnquiryController {

    @Autowired
    EnquiryService enquiryService;

    @PostMapping("/addEnquiry")
    public EnquiryEntity addEnquiry(@RequestBody EnquiryEntity enquiry){

        return enquiryService.addEnquiry(enquiry);

    }

    @GetMapping("/allEnquiries")
    public List<EnquiryEntity> getAllEnquiries(){

        return enquiryService.getAllEnquiries();

    }

    @GetMapping("/getEnquiry/{id}")
    public EnquiryEntity getEnquiryById(@PathVariable int id){

        return enquiryService.getEnquiryById(id);

    }

    @PutMapping("/updateEnquiry/{id}")
    public EnquiryEntity updateEnquiry(@PathVariable int id,
                                       @RequestBody EnquiryEntity enquiry){

        return enquiryService.updateEnquiry(id, enquiry);

    }

    @DeleteMapping("/deleteEnquiry/{id}")
    public String deleteEnquiry(@PathVariable int id){

        return enquiryService.deleteEnquiry(id);

    }

}
