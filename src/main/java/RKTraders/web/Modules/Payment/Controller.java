package RKTraders.web.Modules.Payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/payment")
    @RequiredArgsConstructor
    public class Controller {

        private final PaymentService paymentService;

        @PostMapping("/initiate/{addressId}")
        public ResponseEntity<Entity> initiatePayment(@PathVariable Integer addressId,
                                                      Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.initiatePayment(addressId, authentication.getName())
            );
        }


        @PostMapping("/verify/{paymentId}")
        public ResponseEntity<Entity> verifyPayment(@PathVariable String paymentId,
                                                    @RequestParam String transactionId,
                                                    Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.verifyPayment(paymentId,
                            transactionId,
                            authentication.getName())
            );
        }


        @GetMapping("/my")
        public ResponseEntity<List<Entity>> getMyPayments(Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.getMyPayments(authentication.getName())
            );
        }


        @GetMapping("/{paymentId}")
        public ResponseEntity<Entity> getPaymentById(@PathVariable String paymentId,
                                                     Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.getPaymentById(paymentId,
                            authentication.getName())
            );
        }


        @GetMapping("/all")
        public ResponseEntity<List<Entity>> getAllPayments() {

            return ResponseEntity.ok(
                    paymentService.getAllPayments()
            );
        }


        @GetMapping("/status/{status}")
        public ResponseEntity<List<Entity>> getPaymentsByStatus(@PathVariable PaymentStatusEnum status) {

            return ResponseEntity.ok(
                    paymentService.getPaymentsByStatus(status)
            );
        }


        @GetMapping("/revenue")
        public ResponseEntity<Double> getTotalRevenue() {

            return ResponseEntity.ok(
                    paymentService.getTotalRevenue()
            );
        }

    }
