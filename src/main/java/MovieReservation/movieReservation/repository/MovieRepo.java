package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    @Query("""
select e from Movie e where e.genre.id = :id""")
    Page<Movie> findByFilter(Pageable pageable, Long id);
}