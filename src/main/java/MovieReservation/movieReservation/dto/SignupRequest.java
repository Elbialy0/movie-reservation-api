package MovieReservation.movieReservation.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {

    @NotBlank(message = "First name is required")
    @NotEmpty(message = "First name is required")
    @NotNull(message = "First name is required")
    private String firstName;
    private String lastName;
    @Email(message = "Username must be a valid email address")
    @NotBlank(message = "Username is required")
    @NotEmpty(message = "Username is required")
    @NotNull(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private String phoneNumber;
    private LocalDate birthDate;
}
