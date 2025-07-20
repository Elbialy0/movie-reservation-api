package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.AddMovieRequest;
import MovieReservation.movieReservation.dto.MovieResponse;
import MovieReservation.movieReservation.dto.PageResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<Movie> movie = movieRepo.findByTitle(request.getTitle());
        if (movie.isEmpty()) {
            movieRepo.save(Movie.builder().user(user).title(request.getTitle()).description(request.getDescription())
                    .isAvailable(false).genre(genre).build());
        }
        else {
            throw new MovieException("Movie already exists");
        }


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

    public UrlResource getCover(int id) throws MalformedURLException {
        Movie movie = movieRepo.findById((long) id).orElseThrow(()->new MovieException("Movie not found"));
        return new UrlResource(Path.of(movie.getPoster()).toUri());


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void activateMovie(int movieId) {
        Movie movie = movieRepo.findById((long) movieId).orElseThrow(()->new MovieException(
                "Movie not found"
        ));
        movie.setIsAvailable(true);
        movieRepo.save(movie);

    }

    public PageResponse<MovieResponse> getAllmovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("title").ascending());
        Page<Movie> movies = movieRepo.findAll(pageable);
        List<Movie> moviesList = movies.getContent();
        List<MovieResponse> movieResponses = moviesList.stream().map(mapper::mapToMovieResponse).toList();
        return new PageResponse<>(
                movieResponses,
                movies.getNumber(),
                movies.getNumberOfElements(),
                movies.getSize(),
                movies.getTotalPages(),
                movies.isFirst(),
                movies.isLast()

        );



    }

    public PageResponse<MovieResponse> getByGenre(String filter,int page, int size) {
        Genre genre = genreRepo.findByName(filter);
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Movie> movies = movieRepo.findByFilter(pageable,genre.getId());
        List<MovieResponse> movieResponses = movies.getContent().stream().map(mapper::mapToMovieResponse).toList();
        return new PageResponse<>(
                movieResponses,
                movies.getNumber(),
                movies.getNumberOfElements(),
                movies.getSize(),
                movies.getTotalPages(),
                movies.isFirst(),
                movies.isLast()

        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteMovie(int movieId) {
        Movie movie = movieRepo.findById((long) movieId).orElseThrow(()->new MovieException("Movie not found"));
        movieRepo.delete(movie);
    }
}
