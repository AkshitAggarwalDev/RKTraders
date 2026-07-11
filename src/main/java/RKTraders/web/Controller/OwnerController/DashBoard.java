package RKTraders.web.Controller.OwnerController;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Owner")
public class DashBoard {

    @GetMapping("dashboard")
    public String dashboardapi() {
        return null;
    }
}
