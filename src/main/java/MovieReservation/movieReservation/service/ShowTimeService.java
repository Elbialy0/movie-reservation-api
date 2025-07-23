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

    public String createNewShowTime(ShowTimeRequest request) {
        showTimeRepo.save(ShowTime.builder().time(request.getTime()).reservations(new ArrayList<>()).price(request.getPrice()
        ).movie(movieRepo.findById((long) request.getMovieId()).orElseThrow(()
        ->new RuntimeException("Movie not found"))).hall(hallRepo.findById((long)request.getHallId()).orElseThrow(
                ()-> new RuntimeException("Hall not found")
        )).price(request.getPrice()).build());
        return "Show time created successfully";


    }
}
