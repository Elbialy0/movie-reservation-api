package MovieReservation.movieReservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotBlank(message = "Currency must not be blank")
    private String currency;

    @NotBlank(message = "Payment method must not be blank")
    private String method;

    @NotBlank(message = "Payment intent must not be blank")
    private String intent;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "Payment ID must not be null")
    private Long paymentId;
}
