package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.PageResponse;
import MovieReservation.movieReservation.dto.ShowTimeRequest;
import MovieReservation.movieReservation.dto.ShowTimeResponse;
import MovieReservation.movieReservation.model.ShowTime;
import MovieReservation.movieReservation.service.ShowTimeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/showTime")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<Long > addNewShow(@RequestBody ShowTimeRequest request){
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(showTimeService.createNewShowTime(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<String > updateShow(@PathVariable(name = "id")long id,@RequestBody ShowTimeRequest request){
        showTimeService.updateShow(request,id);
        return ResponseEntity.ok().body("Show time updated successfully");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String > deleteShow(@PathVariable(name = "id")long id){
        showTimeService.deleteShow(id);
        return ResponseEntity.ok().body("Show time deleted successfully");
    }
    @GetMapping("/filter")
    public ResponseEntity<PageResponse<ShowTimeResponse>> getShowsByType(@RequestParam(name = "genre")String genre,
                                                                         @RequestParam(name = "page",defaultValue = "0")int page,
                                                                         @RequestParam(name = "size",defaultValue = "10")int size){
        return ResponseEntity.ok().body(showTimeService.getByType(genre,page,size));
    }
    @GetMapping("/all")
    public ResponseEntity<PageResponse<ShowTimeResponse>> getAllShows(@RequestParam(name = "page", defaultValue = "0")int page,
                                                                      @RequestParam(name = "size", defaultValue = "10")int size){
        return ResponseEntity.ok().body(showTimeService.getAll(page , size));


    }
    @GetMapping("/findMovie")
    public ResponseEntity<PageResponse<ShowTimeResponse>> getByMovie(@RequestParam(name="movieTitle")String movieTitle,
                                                                     @RequestParam(name = "page", defaultValue = "0")int page ,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(showTimeService.getByMovieTitle(movieTitle,page,size));
    }
    @GetMapping("{id}")
    public ResponseEntity<ShowTimeResponse> getShowById(@PathVariable(name = "id")long id){
        return ResponseEntity.ok().body(showTimeService.getShowTime(id));
    }
}
