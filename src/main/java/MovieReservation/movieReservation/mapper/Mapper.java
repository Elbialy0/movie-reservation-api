package MovieReservation.movieReservation.mapper;

import MovieReservation.movieReservation.dto.MovieResponse;
import MovieReservation.movieReservation.dto.ShowTimeResponse;
import MovieReservation.movieReservation.model.Movie;
import MovieReservation.movieReservation.model.ShowTime;
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
    public ShowTimeResponse mapToShowTimeResponse(ShowTime showTime){
        return ShowTimeResponse.builder().time(showTime.getTime()).price(showTime.getPrice())
                .movieTitle(showTime.getMovie().getTitle()).hallName(showTime.getHall().getName()).build();
    }
}
