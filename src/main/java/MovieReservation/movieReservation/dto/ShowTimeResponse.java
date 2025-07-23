package MovieReservation.movieReservation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowTimeResponse {
    private LocalDateTime time;
    private String movieTitle;
    private String hallName;
    private double price;

}
