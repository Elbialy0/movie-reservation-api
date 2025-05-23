package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.AddMovieRequest;
import MovieReservation.movieReservation.exceptions.MovieException;
import MovieReservation.movieReservation.model.Movie;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.MovieRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepo movieRepo;
    private final AuthService authService;
    // here add the movie and make it not available because there are more details must be in the movie
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addMovie( AddMovieRequest request) {
        User user = authService.getAuthentication().orElseThrow(()->new BadCredentialsException("Forbidden"));
        movieRepo.save(Movie.builder().title(request.getTitle())
                .description(request.getDescription()).isAvailable(false).build());
        }

        @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addPoster(Long movieId, MultipartFile poster) {
        User user = authService.getAuthentication().orElseThrow(()->new BadCredentialsException("Forbidden"));

        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(()->new MovieException("Movie not found to add the poster, Please check the id"));
    }
}
