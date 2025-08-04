package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Reservation;
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
select r from Reservation r where r.status = 'CONFIRMED'""")
    Page<Reservation> findAllValidReservations(Pageable pageable);
}