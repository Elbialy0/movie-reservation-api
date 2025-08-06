package MovieReservation.movieReservation.service;

import com.paypal.base.rest.APIContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final APIContext apiContext;
}
