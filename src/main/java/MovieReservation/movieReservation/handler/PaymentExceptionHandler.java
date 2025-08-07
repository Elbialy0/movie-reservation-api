package MovieReservation.movieReservation.handler;

import MovieReservation.movieReservation.dto.ExceptionResponse;
import MovieReservation.movieReservation.exceptions.PaymentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentExceptionHandler {
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ExceptionResponse> handlePaymentException(PaymentException ex , HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                ExceptionResponse.builder().statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .timestamp(java.time.ZonedDateTime.now().toString())
                        .path(request.getRequestURI())
                        .message(ex.getMessage())
                        .error(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase())
                        .build()


        );
    }
}
