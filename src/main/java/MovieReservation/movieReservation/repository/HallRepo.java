package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepo extends JpaRepository<Hall, Long> {
    Hall findByName(String hallName);
}