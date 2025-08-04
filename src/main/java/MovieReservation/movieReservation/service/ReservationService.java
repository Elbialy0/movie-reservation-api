package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.PageResponse;
import MovieReservation.movieReservation.dto.ReservationResponse;
import MovieReservation.movieReservation.exceptions.HallFullCapacityException;
import MovieReservation.movieReservation.exceptions.ShowTimeOutException;
import MovieReservation.movieReservation.mapper.Mapper;
import MovieReservation.movieReservation.model.*;
import MovieReservation.movieReservation.repository.PaymentRepo;
import MovieReservation.movieReservation.repository.ReservationRepo;
import MovieReservation.movieReservation.repository.ShowTimeRepo;
import MovieReservation.movieReservation.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationRepo reservationRepo;
    private final ShowTimeRepo showTimeRepo;
    private final PaymentRepo paymentRepo;
    private final AuthService authService;
    private final EmailService emailService;
    private final UserRepo userRepo;
    private final Mapper mapper;


    public long reserve(long id) {
        ShowTime showTime = showTimeRepo.findById(id).orElseThrow(()->
                new RuntimeException("Show time not found"));
        int reservations = showTime.getReservations().size();
        if(reservations==showTime.getHall().getCapacity()){
            throw new HallFullCapacityException("Hall is full, please try another show");
        }
        if (showTime.getTime().isBefore(LocalDateTime.now())){
            throw new ShowTimeOutException("The show is ended");
        }
        Seat seat = getAvailableSeat(showTime.getHall());
        if(seat!=null){
            seat.setAvailable(false);
        }
        Reservation reservation = new Reservation();
        reservation.setSeat(seat);
        User user = authService.getAuthentication().orElseThrow(()->new RuntimeException("Please login again"));
        reservation.setUser(user);
        reservation.setShowTime(showTime);
        reservation.setStatus(Status.PENDING);
       Payment payment = new Payment();
       payment.setReservation(reservation);
       payment.setAmount(showTime.getPrice());
       payment.setStatus(Status.PENDING);
       payment.setExpirationDate(LocalDateTime.now().minusHours(2));
       paymentRepo.save(payment);
       reservation.setPayment(payment);
       emailService.sendReservationEmail(payment.getId(),reservation);


        return reservationRepo.save(reservation).getId();
    }

    private Seat getAvailableSeat(Hall hall) {
        List<Seat> seats = hall.getSeats();
        for (Seat seat : seats){
            if (seat.isAvailable()) return seat;
        }
        return null;
    }

    public String decline(long id) {
        Reservation reservation =  reservationRepo.findById(id).orElseThrow(()->
                new RuntimeException("Reservation not found"));
        if(reservation.getStatus().equals(Status.PENDING)){
            reservation.setStatus(Status.CANCELED);
            reservation.getSeat().setAvailable(true);
            reservationRepo.save(reservation);
            paymentRepo.delete(reservation.getPayment());
            return "Reservation cancelled successfully";
        }
        else if(reservation.getStatus().equals(Status.CONFIRMED)){
            reservation.setStatus(Status.CANCELED);
            reservation.getSeat().setAvailable(true);
            reservationRepo.save(reservation);
            Payment payment = reservation.getPayment();
            payment.setStatus(Status.RETURN);
            paymentRepo.save(payment);
            return "your reservation is declined successfully the money will rollback in 14 days";
        }
        return "Your Reservation is already canceled";
    }

    public PageResponse<ReservationResponse> getReservations(long id,int page, int size) {
        User user = userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        Pageable pageable = PageRequest.of(page,size);
        Page<Reservation> reservations = reservationRepo.findAllByUser(pageable,id);
        List<ReservationResponse> content = reservations.getContent().stream().map(mapper::mapToReservationResponse).toList();
        return new PageResponse<>(
                content,
                reservations.getNumber(),
                reservations.getNumberOfElements(),
                reservations.getSize(),
                reservations.getTotalPages(),
                reservations.isFirst(),
                reservations.isLast()
        );


    }
}
