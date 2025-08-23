package MovieReservation.movieReservation.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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
    private SeatStatus status;
    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;
}
