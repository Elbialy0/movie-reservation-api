package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.AddMovieRequest;
import MovieReservation.movieReservation.dto.MovieResponse;
import MovieReservation.movieReservation.dto.PageResponse;
import MovieReservation.movieReservation.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")

public class MovieController {
    private final MovieService movieService;


    @PostMapping("/add")
    public ResponseEntity<String> addNewMovie(@RequestBody @Valid AddMovieRequest request){
        movieService.addMovie(request);
        return ResponseEntity.ok("Movie added successfully");

    }
    @PostMapping("/poster")
    public ResponseEntity<String> addPoster(@RequestParam("movie-id")Long movieId,
                                            @RequestParam("poster")MultipartFile poster) throws IOException {
        movieService.addPoster(movieId,poster);
        return ResponseEntity.ok("Poster added successfully");

    }
    @PostMapping("/tailer")
    public ResponseEntity<String> addTailer(@RequestParam("movie-id") Long movieId,
                                            @RequestParam("tailer")MultipartFile tailer) throws IOException {
        movieService.addTailer(movieId,tailer);
        return ResponseEntity.ok("Tailer added successfully");
    }
    @GetMapping("/{movie-id}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable(name = "movie-id") Long movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));

    }
    @GetMapping("/cover/{id}")
    public ResponseEntity<UrlResource> getCover(@PathVariable("id")int id) throws MalformedURLException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(movieService.getCover(id));
    }
    @PutMapping("/activate/{movie-id}")
    public ResponseEntity<String> activateMovie(@PathVariable(name = "movie-id") int movieId){
        movieService.activateMovie(movieId);
        return ResponseEntity.ok().body("Movie activated successfully");

    }
    @GetMapping
    public ResponseEntity<PageResponse<MovieResponse>> getAllMovies(@RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok().body(movieService.getAllMovies(page, size));
        

    }
    @GetMapping("/filter")
    public ResponseEntity<PageResponse<MovieResponse>> getMovieByGenre(@RequestParam(name = "filter")String filter,
                                                                       @RequestParam(defaultValue = "0")int page,
                                                                       @RequestParam(defaultValue = "10") int size){

        return ResponseEntity.ok().body(movieService.getByGenre(filter,page,size));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMovie(@RequestParam(name = "movie-id")int movieId){
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok().body("Movie deleted successfully");
    }
    @GetMapping("rate/{rate}")
    public ResponseEntity<List<MovieResponse>> getByRate(@PathVariable(name = "rate")int rate){
        return ResponseEntity.ok().body(movieService.getByRate(rate));
    }
    @GetMapping("/available")
    public ResponseEntity<PageResponse<MovieResponse> >getAvailableMovies(@RequestParam(name = "page",defaultValue = "0")int page,
                                                                                @RequestParam(name = "size",defaultValue = "10")int size){
        return ResponseEntity.ok().body(movieService.getAvailableMovies(page,size));
    }



}
