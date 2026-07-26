package RKTraders.web.Controller;

import RKTraders.web.Model.Payment;
import RKTraders.web.Service.PaymentService;
import RKTraders.web.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/payment")
    @RequiredArgsConstructor
    public class PaymentController {

        private final PaymentService paymentService;

        @PostMapping("/initiate/{addressId}")
        public ResponseEntity<Payment> initiatePayment(@PathVariable Integer addressId,
                                                       Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.initiatePayment(addressId, authentication.getName())
            );
        }


        @PostMapping("/verify/{paymentId}")
        public ResponseEntity<Payment> verifyPayment(@PathVariable String paymentId,
                                                     @RequestParam String transactionId,
                                                     Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.verifyPayment(paymentId,
                            transactionId,
                            authentication.getName())
            );
        }


        @GetMapping("/my")
        public ResponseEntity<List<Payment>> getMyPayments(Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.getMyPayments(authentication.getName())
            );
        }


        @GetMapping("/{paymentId}")
        public ResponseEntity<Payment> getPaymentById(@PathVariable String paymentId,
                                                      Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.getPaymentById(paymentId,
                            authentication.getName())
            );
        }


        @GetMapping("/all")
        public ResponseEntity<List<Payment>> getAllPayments() {

            return ResponseEntity.ok(
                    paymentService.getAllPayments()
            );
        }


        @GetMapping("/status/{status}")
        public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {

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
