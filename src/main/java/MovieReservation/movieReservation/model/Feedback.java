package MovieReservation.movieReservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Feedback {
    @Id
    @GeneratedValue
    private Long id;
    private String comment;
    private int rate;

    @ManyToOne
    private User user;
    @ManyToOne
    private Movie movie;
}
