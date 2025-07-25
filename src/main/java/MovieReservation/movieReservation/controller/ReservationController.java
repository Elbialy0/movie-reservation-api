package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/reserve/{id}")
    public ResponseEntity<String > reserve(@PathVariable(name = "id")long id){
        long reservationId = reservationService.reserve(id);
        return ResponseEntity.ok().body("Reservation id : "+reservationId);

    }



}
