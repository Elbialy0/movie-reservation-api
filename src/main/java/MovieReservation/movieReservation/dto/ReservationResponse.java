package MovieReservation.movieReservation.dto;

import MovieReservation.movieReservation.model.Seat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponse {
    private long seatId;
    private String movieTitle;
    private String hallName;
    private double price;
    private LocalDateTime time;

}
