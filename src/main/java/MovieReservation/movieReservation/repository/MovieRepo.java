package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Movie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    @Query("""
select e from Movie e where e.genre.id = :id""")
    Page<Movie> findByFilter(Pageable pageable, Long id);


    @Query("""
select e from Movie e where e.isAvailable = true""")
    Page<Movie> findAvailableMovies(Pageable pageable);

    Optional<Movie> findByTitle(@NotNull(message = "The movie must have title") @NotEmpty(message = "The movie must have title") @NotBlank(message = "The movie must have title") String title);
}