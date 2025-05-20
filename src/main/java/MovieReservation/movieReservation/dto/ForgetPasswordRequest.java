package MovieReservation.movieReservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordRequest {
    @Email(message = "Username must be a valid email address")
    @NotBlank(message = "Username is required")
    @NotEmpty(message = "Username is required")
    @NotNull(message = "Username is required")
    private String email;
}
