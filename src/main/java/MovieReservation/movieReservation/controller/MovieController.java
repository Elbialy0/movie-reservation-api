package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.AddMovieRequest;
import MovieReservation.movieReservation.dto.MovieResponse;
import MovieReservation.movieReservation.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")

public class MovieController {
    private final MovieService movieService;
    // todo security configurations
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
    @GetMapping("/cover/{url}")
    public ResponseEntity<UrlResource> getCover(@PathVariable("url")String url) throws MalformedURLException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(movieService.getCover(url));
    }
    @PutMapping("/activate/{movie-id}")
    public ResponseEntity<String> activateMovie(@PathVariable(name = "movie-id") int movieId){
        movieService.activateMovie(movieId);
        return ResponseEntity.ok().body("Movie activated successfully");

    }



}
