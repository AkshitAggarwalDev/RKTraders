package RKTraders.web.Modules.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class Controller {


    @Autowired
    private Repo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private RKTraders.web.Modules.Customer.Repo customerRepo;

    @Autowired
    private CartService cartService;

    @PostMapping("add/{productId}")
    public ResponseEntity<String> addToCart(
            Authentication authentication,
            @PathVariable int productId,
            @RequestParam int quantity) {
        System.out.println("Inside Cart Controller");

        return ResponseEntity.ok(

                cartService.addToCart(

                        authentication.getName(),

                        productId,

                        quantity

                )

        );
    }


    @GetMapping("viewItems")
    public ResponseEntity<List<CartItemEntity>> viewCart(Authentication authentication) {

        return ResponseEntity.ok(
                cartService.viewCart(authentication.getName())
        );
    }


    @PutMapping("update/{cartItemId}")
    public ResponseEntity<String> updateQuantity(
            Authentication authentication,
            @PathVariable int cartItemId,
            @RequestParam int quantity) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        authentication.getName(),
                        cartItemId,
                        quantity
                )
        );
    }


    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeItem(
            Authentication authentication,
            @PathVariable int cartItemId) {

        return ResponseEntity.ok(
                cartService.removeItem(
                        authentication.getName(),
                        cartItemId
                )
        );
    }

    @DeleteMapping("clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {

        return ResponseEntity.ok(
                cartService.clearCart(authentication.getName())
        );
    }

    @GetMapping("/total")
    public ResponseEntity<Double> cartTotal(Authentication authentication) {

        return ResponseEntity.ok(
                cartService.cartTotal(authentication.getName())
        );
    }
}
