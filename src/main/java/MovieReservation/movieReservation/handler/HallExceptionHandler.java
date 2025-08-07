package MovieReservation.movieReservation.handler;

import MovieReservation.movieReservation.dto.ExceptionResponse;
import MovieReservation.movieReservation.exceptions.HallFullCapacityException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HallExceptionHandler {
    @ExceptionHandler(HallFullCapacityException.class)
    public ResponseEntity<ExceptionResponse> handleHallException(HallFullCapacityException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                ExceptionResponse.builder()
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .error(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .timestamp(java.time.ZonedDateTime.now().toString())
                        .build()

        );
    }
}
