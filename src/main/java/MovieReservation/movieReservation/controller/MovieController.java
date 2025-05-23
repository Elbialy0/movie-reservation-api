package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.AddMovieRequest;
import MovieReservation.movieReservation.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/poster}")
    public ResponseEntity<String> addPoster(@RequestParam("movie-id")Long movieId,
                                            @RequestParam("poster")MultipartFile poster){
        movieService.addPoster(movieId,poster);
        return ResponseEntity.ok("Poster added successfully");

    }
}
