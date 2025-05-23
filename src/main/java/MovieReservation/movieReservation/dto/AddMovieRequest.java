package MovieReservation.movieReservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddMovieRequest {
    @NotNull(message = "The movie must have title")
    @NotEmpty(message = "The movie must have title")
    @NotBlank(message = "The movie must have title")
    String title;
    @NotNull(message = "The movie must have description")
    @NotEmpty(message = "The movie must have description")
    @NotBlank(message = "The movie must have description")
    String description;
}
