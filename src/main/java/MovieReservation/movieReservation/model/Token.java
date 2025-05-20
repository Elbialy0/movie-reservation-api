package MovieReservation.movieReservation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Token {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    private LocalDateTime expirationDate;

}
