package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.PageResponse;

public interface SeatService {
    public void addSeat(String hallName,String idempotencyKey);

    public  void delete(Long id);

    public PageResponse<Long> getAvailableSeats(String hallName,int page,int size);
}
