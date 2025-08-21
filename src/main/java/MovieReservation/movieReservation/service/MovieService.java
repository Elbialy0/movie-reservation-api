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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    private final MovieRepo movieRepo;
    private final AuthService authService;
    private final GenreRepo genreRepo;
    private final Mapper mapper;
    private static final String UPLOAD_DIR = "D:\\upload_dir";

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Caching(evict = {
            @CacheEvict(value = "moviesByGenre", allEntries = true),
            @CacheEvict(value = "availableMovies", allEntries = true)
    })
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
        @Caching(evict = {
                @CacheEvict(value = "moviesByGenre", allEntries = true),
                @CacheEvict(value = "availableMovies", allEntries = true)
        }, put = {
                @CachePut(value = "movie", key = "#movieId")
        })
    public MovieResponse addPoster(Long movieId, MultipartFile poster) throws IOException {
        User user = authService.getAuthentication().orElseThrow(()->new BadCredentialsException("Forbidden"));

        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(()->new MovieException("Movie not found to add the poster, Please check the id"));

            movie.setPoster(saveFile(poster));
            movieRepo.save(movie);
            return mapper.mapToMovieResponse(movie);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Caching(evict = {
            @CacheEvict(value = "moviesByGenre", allEntries = true),
            @CacheEvict(value = "availableMovies", allEntries = true)
    }, put = {
            @CachePut(value = "movie", key = "#movieId")
    })
    public MovieResponse addTailer(Long movieId, MultipartFile tailer) throws IOException {
        User user = authService.getAuthentication().orElseThrow(()->new BadCredentialsException("Forbidden"));
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(()->new MovieException("Movie not found to add the tailer, Please check the id"));
        movie.setTrailer(saveFile(tailer));
        return mapper.mapToMovieResponse(movie);
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

    @Cacheable(value = "movie", key = "#movieId")
    public MovieResponse getMovie(Long movieId) {
        Movie movie = movieRepo.findById(movieId).orElseThrow(()->new MovieException("Movie not found"));
        return mapper.mapToMovieResponse(movie);
    }


    public UrlResource getCover(int id) throws MalformedURLException {
        Movie movie = movieRepo.findById((long) id).orElseThrow(()->new MovieException("Movie not found"));
        return new UrlResource(Path.of(movie.getPoster()).toUri());


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Caching(evict = {
            @CacheEvict(value = "moviesByGenre", allEntries = true),
            @CacheEvict(value = "availableMovies", allEntries = true)
    }, put = {
            @CachePut(value = "movie", key = "#movieId")
    })
    public MovieResponse activateMovie(int movieId) {
        Movie movie = movieRepo.findById((long) movieId).orElseThrow(()->new MovieException(
                "Movie not found"
        ));
        movie.setIsAvailable(true);
        movieRepo.save(movie);
        return mapper.mapToMovieResponse(movie);

    }

    @Cacheable(value = "movies", key = "#page + '-' + #size")
    public PageResponse<MovieResponse> getAllMovies(int page, int size) {
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

    @Cacheable(value = "moviesByGenre", key = " #filter + '-' +#page + '-' + #size")
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
    @CacheEvict(
            value = { "movie", "movies", "moviesByGenre", "availableMovies" },
            allEntries = true
    )
    public void deleteMovie(int movieId) {
        Movie movie = movieRepo.findById((long) movieId).orElseThrow(()->new MovieException("Movie not found"));
        movieRepo.delete(movie);
    }

    public List<MovieResponse> getByRate(int rate) {
        List<Movie> movies = movieRepo.findAll();
        return movies.stream().filter(m->m.getAverageRate()>=rate)
                .map(mapper::mapToMovieResponse).toList();

    }

    @Cacheable(value = "availableMovies", key = "#page + '-' + #size")
    public PageResponse<MovieResponse> getAvailableMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("title").ascending());
        Page<Movie> movies = movieRepo.findAvailableMovies(pageable);
        List<MovieResponse> content = movies.getContent().stream().map(mapper::mapToMovieResponse).toList();
        return new PageResponse<>(
                content,
                movies.getNumber(),
                movies.getNumberOfElements(),
                movies.getSize(),
                movies.getTotalPages(),
                movies.isFirst(),
                movies.isLast()
        );


    }

}
