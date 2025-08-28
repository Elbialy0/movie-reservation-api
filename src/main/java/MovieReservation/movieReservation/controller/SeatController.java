package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.PageResponse;
import MovieReservation.movieReservation.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewSeat(@RequestParam("hall-name") String hallName){
        seatService.addSeat(hallName);
    }

    @DeleteMapping("/delete/{seat-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSeat(@PathVariable("seat-id")Long id){
        seatService.delete(id);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Long>> getAvailableSeats(@RequestParam("hall-name")String hallName,
                                                                @RequestParam(name = "page" , defaultValue = "0")int page,
                                                                @RequestParam(name = "size", defaultValue = "10")int size){
        return ResponseEntity.ok().body(seatService.getAvailableSeats(hallName,page,size));
    }

}
