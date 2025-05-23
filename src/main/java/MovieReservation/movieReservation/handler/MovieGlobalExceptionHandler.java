package MovieReservation.movieReservation.handler;

import MovieReservation.movieReservation.dto.ExceptionResponse;
import MovieReservation.movieReservation.exceptions.MovieException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MovieGlobalExceptionHandler {
    @ExceptionHandler(MovieException.class)
    public ResponseEntity<ExceptionResponse> handleMovieException(MovieException ex, HttpServletRequest request){
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
