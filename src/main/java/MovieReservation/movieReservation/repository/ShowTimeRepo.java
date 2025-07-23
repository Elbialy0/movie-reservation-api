package MovieReservation.movieReservation.repository;

import MovieReservation.movieReservation.model.ShowTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowTimeRepo extends JpaRepository<ShowTime, Long> {
    @Query("""
select s from ShowTime s where s.movie.genre.name = :genre""")

    Page<ShowTime> findAllByGenre(Pageable pageable, String genre);
}
