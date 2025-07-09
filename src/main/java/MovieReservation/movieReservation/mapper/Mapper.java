package MovieReservation.movieReservation.mapper;

import MovieReservation.movieReservation.dto.MovieResponse;
import MovieReservation.movieReservation.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Mapper {
    public MovieResponse mapToMovieResponse(Movie movie){
        return MovieResponse.builder().title(movie.getTitle()).description(movie.getDescription())
                .genre(movie.getGenre().getName()).poster(movie.getPoster())
                .tailer(movie.getTrailer()).build();
    }
}
