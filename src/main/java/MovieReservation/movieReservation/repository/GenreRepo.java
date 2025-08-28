package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends JpaRepository<Genre, Long> {
    Genre findByName( String genre);
}