package MovieReservation.movieReservation.handler;

import MovieReservation.movieReservation.dto.ExceptionResponse;
import MovieReservation.movieReservation.exceptions.MailSendingExceptions;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MailExceptionHandler {
    @ExceptionHandler(MailSendingExceptions.class)
    public ResponseEntity<ExceptionResponse> mailSendingExceptionHandler(MailSendingExceptions ex, HttpServletRequest request){
        return ResponseEntity.badRequest().body(
                ExceptionResponse.builder().statusCode(400)
                        .message(ex.getMessage())
                        .error(400+" Bad Request")
                        .path(request.getRequestURI())
                        .timestamp(java.time.ZonedDateTime.now().toString())
                        .build()
        );
    }
}
