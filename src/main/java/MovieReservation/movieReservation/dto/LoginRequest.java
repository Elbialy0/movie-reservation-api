package MovieReservation.movieReservation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "Username must be a valid email address")
    @NotBlank(message = "Username is required")
    @NotEmpty(message = "Username is required")
    @NotNull(message = "Username is required")

    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")

    private String password;
}
