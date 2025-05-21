package MovieReservation.movieReservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest {
    @NotNull(message = "Jwt token is required")
    @NotEmpty(message = "Jwt token is required")
    @NotBlank(message = "Jwt token is required")
    String jwtToken;
    @NotNull(message = "Refresh token is required")
    @NotEmpty(message = "Refresh token is required")
    @NotBlank(message = "Refresh token is required")
    String refreshToken;

}
