package MovieReservation.movieReservation.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Hall {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int capacity;
    private String location;
    @OneToMany(mappedBy = "hall")
    private List<ShowTime> showTimes;
    @OneToMany(mappedBy = "hall")
    private List<Seat> seats;
}
