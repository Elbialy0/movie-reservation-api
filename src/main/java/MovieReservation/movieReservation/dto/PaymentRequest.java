package MovieReservation.movieReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {
    private String currency;
    private String method;
    private String intent;
    private String description;
    private Long paymentId;

}
