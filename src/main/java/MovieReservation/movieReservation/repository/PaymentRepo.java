package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Payment findBypaypalId(String paymentId);
}