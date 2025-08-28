package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.PageResponse;
import MovieReservation.movieReservation.model.Hall;
import MovieReservation.movieReservation.model.Seat;
import MovieReservation.movieReservation.model.SeatStatus;
import MovieReservation.movieReservation.repository.HallRepo;
import MovieReservation.movieReservation.repository.SeatRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatServiceImp implements SeatService{
    private final SeatRepo seatRepo;
    private final HallRepo hallRepo;

    @Override
    public void addSeat(String hallName) {
        Hall hall = hallRepo.findByName(hallName);
        Seat seat = new Seat();
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setHall(hall);
        seatRepo.save(seat);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Seat seat = seatRepo.findById(id).orElseThrow(()->new RuntimeException("Seat not found"));
        seatRepo.delete(seat);
    }
    @Override
    public PageResponse<Long> getAvailableSeats(String hallName,int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Long> seats = seatRepo.findAvailableSeats(pageable,hallRepo.findByName(hallName)
                ,SeatStatus.AVAILABLE,SeatStatus.HELD);
        return new PageResponse<>(
                seats.getContent(),
                seats.getNumber(),
                seats.getNumberOfElements(),
                seats.getSize(),
                seats.getTotalPages(),
                seats.isFirst(),
                seats.isLast()
        );
    }

}
