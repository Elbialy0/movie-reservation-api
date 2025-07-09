package MovieReservation.movieReservation.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {
    private String title;
    private String description;
    private String genre;
    private String poster;
    private String tailer;
}
