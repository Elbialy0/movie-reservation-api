package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends JpaRepository<Genre, Long> {
    Genre findByName( String genre);
}