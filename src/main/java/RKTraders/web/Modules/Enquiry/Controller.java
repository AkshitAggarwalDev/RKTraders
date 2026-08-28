package RKTraders.web.Modules.Enquiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enquiries")
public class Controller {

    @Autowired
    EnquiryService enquiryService;

    @PostMapping("/addEnquiry")
    public Entity addEnquiry(@RequestBody Entity enquiry){

        return enquiryService.addEnquiry(enquiry);

    }

    @GetMapping("/allEnquiries")
    public List<Entity> getAllEnquiries(){

        return enquiryService.getAllEnquiries();

    }

    @GetMapping("/getEnquiry/{id}")
    public Entity getEnquiryById(@PathVariable int id){

        return enquiryService.getEnquiryById(id);

    }

    @PutMapping("/updateEnquiry/{id}")
    public Entity updateEnquiry(@PathVariable int id,
                                @RequestBody Entity enquiry){

        return enquiryService.updateEnquiry(id, enquiry);

    }

    @DeleteMapping("/deleteEnquiry/{id}")
    public String deleteEnquiry(@PathVariable int id){

        return enquiryService.deleteEnquiry(id);

    }

}
