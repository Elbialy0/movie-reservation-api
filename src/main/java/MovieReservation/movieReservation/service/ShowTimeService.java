package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.ShowTimeRequest;
import MovieReservation.movieReservation.model.ShowTime;
import MovieReservation.movieReservation.repository.HallRepo;
import MovieReservation.movieReservation.repository.MovieRepo;
import MovieReservation.movieReservation.repository.PaymentRepo;
import MovieReservation.movieReservation.repository.ShowTimeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowTimeService {
    private final ShowTimeRepo showTimeRepo;
    private final HallRepo hallRepo;
    private final EmailService emailService;
    private final MovieRepo movieRepo;
    private final PaymentRepo paymentRepo;

    public long createNewShowTime(ShowTimeRequest request) {
      return    showTimeRepo.save(ShowTime.builder().time(request.getTime()).reservations(new ArrayList<>()).price(request.getPrice()
        ).movie(movieRepo.findById((long) request.getMovieId()).orElseThrow(()
        ->new RuntimeException("Movie not found"))).hall(hallRepo.findById((long)request.getHallId()).orElseThrow(
                ()-> new RuntimeException("Hall not found")
        )).price(request.getPrice()).build()).getId();



    }

    public void updateShow(ShowTimeRequest request,long id) {
        ShowTime showTime = showTimeRepo.findById(id).orElseThrow(
                ()-> new RuntimeException("Show time not found")
        );
        showTime.setTime(request.getTime());
        showTime.setHall(hallRepo.findById((long)request.getHallId()).orElseThrow(
                ()-> new RuntimeException("Hall not found")
        ));
        showTime.setMovie(movieRepo.findById((long) request.getMovieId()).orElseThrow(
                ()-> new RuntimeException("Movie not found")
        ));
        showTime.setPrice(request.getPrice());
        showTimeRepo.save(showTime);
        log.info("Show time updated successfully");

    }

    public void deleteShow(long id) {
        ShowTime showTime = showTimeRepo.findById(id).orElseThrow(
                ()-> new RuntimeException("Show time not found")
        );
        showTimeRepo.delete(showTime);
        log.info("Show time deleted successfully");
    }
}
