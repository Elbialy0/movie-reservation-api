package MovieReservation.movieReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeRequest {
    private LocalDateTime time;
    private int movieId;
    private int hallId;
    private double price;

}
