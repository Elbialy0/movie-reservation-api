package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.AddMovieRequest;
import MovieReservation.movieReservation.dto.MovieResponse;
import MovieReservation.movieReservation.exceptions.MovieException;
import MovieReservation.movieReservation.mapper.Mapper;
import MovieReservation.movieReservation.model.Genre;
import MovieReservation.movieReservation.model.Movie;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.GenreRepo;
import MovieReservation.movieReservation.repository.MovieRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    private final MovieRepo movieRepo;
    private final AuthService authService;
    private final GenreRepo genreRepo;
    private final Mapper mapper;
    private static final String UPLOAD_DIR = "D:\\upload_dir";
    // here add the movie and make it not available because there are more details must be in the movie
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addMovie( AddMovieRequest request) {
        User user = authService.getAuthentication().orElseThrow(()->new BadCredentialsException("Forbidden"));
        Genre genre = genreRepo.findByName(request.getGenre());
        movieRepo.save(Movie.builder().user(user).title(request.getTitle()).description(request.getDescription())
                .isAvailable(false).genre(genre).build());

//        movieRepo.save(Movie.builder().title(request.getTitle())
//                .description(request.getDescription()).isAvailable(false).build());
        }
        @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addPoster(Long movieId, MultipartFile poster) throws IOException {
        User user = authService.getAuthentication().orElseThrow(()->new BadCredentialsException("Forbidden"));

        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(()->new MovieException("Movie not found to add the poster, Please check the id"));

            movie.setPoster(saveFile(poster));
            movieRepo.save(movie);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addTailer(Long movieId, MultipartFile tailer) throws IOException {
        User user = authService.getAuthentication().orElseThrow(()->new BadCredentialsException("Forbidden"));
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(()->new MovieException("Movie not found to add the tailer, Please check the id"));
        movie.setTrailer(saveFile(tailer));
    }

    public String saveFile(MultipartFile poster) throws IOException {
        if(poster==null){
            throw new IOException("There is a problem in upload");
        }
        var savedFile = new File(UPLOAD_DIR + File.separator + poster.getOriginalFilename());
        if (!Objects.equals(savedFile.getParent(), UPLOAD_DIR)){ // security check
            throw new SecurityException("Unsafe file path");
        }
        Files.copy(poster.getInputStream(), savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return savedFile.getAbsolutePath();
    }

    public MovieResponse getMovie(Long movieId) {
        Movie movie = movieRepo.findById(movieId).orElseThrow(()->new MovieException("Movie not found"));
        return mapper.mapToMovieResponse(movie);
    }

    public UrlResource getCover(String url) throws MalformedURLException {
        Path path = Paths.get(url);
        return new UrlResource(path.toUri());

    }

    public void activateMovie(int movieId) {
        Movie movie = movieRepo.findById((long) movieId).orElseThrow(()->new MovieException(
                "Movie not found"
        ));
        movie.setIsAvailable(true);
        movieRepo.save(movie);

    }
}
