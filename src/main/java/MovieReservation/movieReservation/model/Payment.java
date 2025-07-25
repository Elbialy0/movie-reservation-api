package MovieReservation.movieReservation.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    private double amount;
    private String paymentMethod;


    @Column(length = 255)
    private String cardNumber;

    @Column(length = 255)
    private String cardHolderName;

    @Column(length = 10)
    private LocalDateTime expirationDate;
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    private Status status;
}
