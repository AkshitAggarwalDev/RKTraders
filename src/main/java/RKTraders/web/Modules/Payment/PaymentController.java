package RKTraders.web.Modules.Payment;

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
        public ResponseEntity<PaymentEntity> initiatePayment(@PathVariable Integer addressId,
                                                             Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.initiatePayment(addressId, authentication.getName())
            );
        }


        @PostMapping("/verify/{paymentId}")
        public ResponseEntity<PaymentEntity> verifyPayment(@PathVariable String paymentId,
                                                           @RequestParam String transactionId,
                                                           Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.verifyPayment(paymentId,
                            transactionId,
                            authentication.getName())
            );
        }


        @GetMapping("/my")
        public ResponseEntity<List<PaymentEntity>> getMyPayments(Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.getMyPayments(authentication.getName())
            );
        }


        @GetMapping("/{paymentId}")
        public ResponseEntity<PaymentEntity> getPaymentById(@PathVariable String paymentId,
                                                            Authentication authentication) {

            return ResponseEntity.ok(
                    paymentService.getPaymentById(paymentId,
                            authentication.getName())
            );
        }


        @GetMapping("/all")
        public ResponseEntity<List<PaymentEntity>> getAllPayments() {

            return ResponseEntity.ok(
                    paymentService.getAllPayments()
            );
        }


        @GetMapping("/status/{status}")
        public ResponseEntity<List<PaymentEntity>> getPaymentsByStatus(@PathVariable PaymentStatusEnum status) {

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
