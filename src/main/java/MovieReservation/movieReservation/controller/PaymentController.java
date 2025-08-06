package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.PaymentRequest;
import MovieReservation.movieReservation.dto.PaymentResponse;
import MovieReservation.movieReservation.service.PaymentService;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@Valid PaymentRequest paymentRequest) throws PayPalRESTException {
      return   ResponseEntity.ok().body(paymentService.pay(paymentRequest));
    }
}