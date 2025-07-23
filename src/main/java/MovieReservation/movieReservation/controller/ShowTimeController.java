package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.ShowTimeRequest;
import MovieReservation.movieReservation.model.ShowTime;
import MovieReservation.movieReservation.service.ShowTimeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/showTime")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<String > addNewShow(@RequestBody ShowTimeRequest request){
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(showTimeService.createNewShowTime(request));
    }
}
