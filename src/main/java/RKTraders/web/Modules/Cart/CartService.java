package RKTraders.web.Modules.Cart;

import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {



    @Autowired
    private Repo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private RKTraders.web.Modules.Customer.Repo customerRepo;

    @Autowired
    private RKTraders.web.Modules.Product.Repo productRepo;


    public String addToCart(String email, int productId, int quantity) {
        System.out.println("Inside Add To Cart Service");

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        RKTraders.web.Modules.Product.Entity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock available");
        }

        Entity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseGet(() -> {

                    Entity newCart = new Entity();

                    newCart.setCustomer(customer);

                    return cartRepo.save(newCart);

                });

        CartItemEntity existingItem = cartItemRepo
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingItem != null) {

            int newQuantity = existingItem.getQuantity() + quantity;

            if (newQuantity > product.getStock()) {
                throw new RuntimeException("Requested quantity exceeds available stock");
            }

            existingItem.setQuantity(newQuantity);

            cartItemRepo.save(existingItem);

            return "Quantity Updated Successfully";
        }

        CartItemEntity cartItem = new CartItemEntity();

        cartItem.setCart(cart);

        cartItem.setProduct(product);

        cartItem.setQuantity(quantity);

        cartItemRepo.save(cartItem);

        return "Product Added To Cart Successfully";
    }

    public List<CartItemEntity> viewCart(String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItemEntity> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        return cartItems;
    }


    public String updateQuantity(String email, int cartItemId, int quantity) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItemEntity cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));

        if (cartItem.getCart().getId() != cart.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        if (quantity > cartItem.getProduct().getStock()) {
            throw new BadRequestException("Insufficient stock available");
        }

        cartItem.setQuantity(quantity);

        cartItemRepo.save(cartItem);

        return "Quantity Updated Successfully";
    }

    public String removeItem(String email, int cartItemId) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItemEntity cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));

        if (cartItem.getCart().getId() != cart.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        cartItemRepo.delete(cartItem);

        return "Item Removed Successfully";
    }

    public String clearCart(String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItemEntity> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is already empty");
        }

        cartItemRepo.deleteAll(cartItems);

        return "Cart Cleared Successfully";
    }

    public double cartTotal(String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItemEntity> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        double total = 0;

        for (CartItemEntity item : cartItems) {

            total += item.getProduct().getPrice() * item.getQuantity();

        }

        return total;
    }
}
