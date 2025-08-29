package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.PageResponse;
import MovieReservation.movieReservation.dto.ReservationResponse;
import MovieReservation.movieReservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reserve/{id}")
    public ResponseEntity<String > reserve(@PathVariable(name = "id")long id,
                                           @RequestParam(name = "seat-id")long seatId,
                                           @RequestHeader("Idempotency-Key")String idempotencyKey){
        long reservationId = reservationService.reserve(id,seatId,idempotencyKey);
        return ResponseEntity.ok().body("Reservation id : "+reservationId);

    }
    @GetMapping("/decline/{id}")
    public ResponseEntity<String> decline(@PathVariable(name = "id")long id){
        return ResponseEntity.ok().body(reservationService.decline(id));
    }
    @GetMapping("/reservations/{id}")
    public ResponseEntity<PageResponse<ReservationResponse>> getReservations(@PathVariable(name = "id")long id,
                                                                             @RequestParam(name = "page",defaultValue = "0")int page,
                                                                             @RequestParam(name = "size",defaultValue = "10")int size){

        return ResponseEntity.ok().body(reservationService.getReservations(id,page,size));
    }

    @GetMapping("/valid")
    public ResponseEntity<PageResponse<ReservationResponse>> getValidReservations(@RequestParam(name = "page",defaultValue = "0")int page,
                                                                                  @RequestParam(name = "size",defaultValue = "10")int size){
        return ResponseEntity.ok().body(reservationService.getValidReservations(page,size));
    }



}
