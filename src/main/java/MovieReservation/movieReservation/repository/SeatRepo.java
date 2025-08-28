package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Hall;
import MovieReservation.movieReservation.model.Seat;
import MovieReservation.movieReservation.model.SeatStatus;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Seat> findById(@NonNull Long aLong);


    @Query("""
SELECT s.id FROM Seat s WHERE Seat.hall=:hall
 AND ((s.status=:seatStatus)OR (s.status=:seatStatus1 AND 
 FUNCTION('DATEDIFF', CURRENT_DATE, s.lastModified) >= 5))""")
  Page<Long> findAvailableSeats(Pageable pageable, Hall hall, SeatStatus seatStatus, SeatStatus seatStatus1);

}