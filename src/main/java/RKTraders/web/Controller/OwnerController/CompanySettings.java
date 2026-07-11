package RKTraders.web.Controller.OwnerController;

import RKTraders.web.DTO.OwnerLoginDTO;
import RKTraders.web.DTO.PasswordUpdateDTO;
import RKTraders.web.Model.Owner;
import RKTraders.web.Service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("owner/settings")
public class CompanySettings {

    @Autowired
    private OwnerService ownerService;


    @PostMapping("login")
    public String loginOwner(@RequestBody OwnerLoginDTO loginRequest) {

        return ownerService.loginOwner(loginRequest);
    }


    @GetMapping("profile")
    public Owner ownerProfile(Authentication authentication) {

        return ownerService.getProfile(authentication.getName());
    }


    @PutMapping("updateProfile")
    public Owner updateOwnerProfile(Authentication authentication,
                                    @RequestBody Owner updatedOwner) {

        return ownerService.updateProfile(
                authentication.getName(),
                updatedOwner
        );
    }


    @PatchMapping("updatePassword")
    public String ownerPassword(Authentication authentication,
                                @RequestBody PasswordUpdateDTO request) {

        return ownerService.updatePassword(
                authentication.getName(),
                request
        );
    }


    @PatchMapping("updateMobileNumber")
    public String ownerMobileNo(Authentication authentication,
                                @RequestBody String mobileNo) {

        return ownerService.updateMobile(
                authentication.getName(),
                mobileNo
        );
    }

}