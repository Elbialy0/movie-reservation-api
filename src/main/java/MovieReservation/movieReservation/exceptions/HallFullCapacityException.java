package MovieReservation.movieReservation.exceptions;

public class HallFullCapacityException extends RuntimeException {
    public HallFullCapacityException(String message) {
        super(message);
    }
}
