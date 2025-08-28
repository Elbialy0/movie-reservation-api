package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Hall;
import MovieReservation.movieReservation.model.Seat;
import MovieReservation.movieReservation.model.SeatStatus;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Seat> findById(@NonNull Long aLong);


    @Query("""
SELECT s.id FROM Seat s
WHERE s.hall = :hall
AND (
    s.status = :status1
    OR (
        s.status = :status2
        AND (CURRENT_DATE - s.lastModified) >= 5
    )
)
""")
    Page<Long> findAvailableSeats(Pageable pageable,
                                  @Param("hall") Hall hall,
                                  @Param("status1") SeatStatus status1,
                                  @Param("status2") SeatStatus status2);
}
