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
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    private int amount;
    private String paymentMethod;

    // TODO: These fields should be encrypted before storing in the database
    // Consider using a secure encryption library or a dedicated payment processing service
    @Column(length = 255)
    private String cardNumber;

    @Column(length = 255)
    private String cardHolderName;

    @Column(length = 10)
    private String expirationDate;
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    private Status status;
}
