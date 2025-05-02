package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowTimeRepo extends JpaRepository<ShowTime, Long> {
}
