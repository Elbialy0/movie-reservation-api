package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.PaymentRequest;
import MovieReservation.movieReservation.dto.PaymentResponse;
import MovieReservation.movieReservation.service.PaymentService;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@Valid @RequestBody PaymentRequest paymentRequest) throws PayPalRESTException {
      return   ResponseEntity.ok().body(paymentService.pay(paymentRequest));
    }
    @GetMapping("/sucess")
    public ResponseEntity<String> successfulPayment(@RequestParam("paymentId") String paymentId,
                                                    @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        return ResponseEntity.ok().body(paymentService.successfulPayment(paymentId,payerId));
    }
    @GetMapping("/cancel")
    public ResponseEntity<String> cancel(){
        return ResponseEntity.ok("Payment canceled by user.");

    }
}