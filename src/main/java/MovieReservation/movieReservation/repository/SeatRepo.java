package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Seat;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Seat> findById(@NonNull  Long aLong);
}
