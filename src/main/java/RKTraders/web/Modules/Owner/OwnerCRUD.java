package RKTraders.web.Modules.Owner;

import RKTraders.web.Modules.Customer.PasswordUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("owner/settings")
public class OwnerCRUD {

    @Autowired
    private OwnerService ownerService;


    @PostMapping("login")
    public String loginOwner(@RequestBody OwnerLoginDTO loginRequest) {

        return ownerService.loginOwner(loginRequest);
    }


    @GetMapping("profile")
    public Entity ownerProfile(Authentication authentication) {

        return ownerService.getProfile(authentication.getName());
    }


    @PutMapping("updateProfile")
    public Entity updateOwnerProfile(Authentication authentication,
                                     @RequestBody Entity updatedOwner) {

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
