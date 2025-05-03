package MovieReservation.movieReservation.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Seat {
    @Id
    @GeneratedValue
    private Long id;
    private boolean available;
    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;
}
