package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Reservation;
import MovieReservation.movieReservation.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    @Query("""
select r from Reservation r where r.user.id = :id""")
    Page<Reservation> findAllByUser(Pageable pageable, long id);

    @Query("""
SELECT r FROM Reservation r WHERE r.status = :status""")
    Page<Reservation> findReservationsBasedOnStatus(Pageable pageable, Status status);
}