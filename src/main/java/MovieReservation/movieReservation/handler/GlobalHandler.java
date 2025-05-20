package MovieReservation.movieReservation.handler;

import MovieReservation.movieReservation.dto.ExceptionResponse;
import MovieReservation.movieReservation.exceptions.InvalidJwtToken;
import MovieReservation.movieReservation.exceptions.SignupException;
import MovieReservation.movieReservation.exceptions.UsernameNotFoundException;
import MovieReservation.movieReservation.exceptions.VerificationTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionResponse.builder().statusCode(HttpStatus.NOT_FOUND.value())
                                .message(ex.getMessage())
                                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                                .path(request.getRequestURI())
                                                        .timestamp(java.time.ZonedDateTime.now().toString())
                        .build()
        );
    }
    @ExceptionHandler(InvalidJwtToken.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtException(InvalidJwtToken ex , HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ExceptionResponse.builder().statusCode(HttpStatus.UNAUTHORIZED.value())
                        .message(ex.getMessage())
                        .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .path(request.getRequestURI())
                        .timestamp(java.time.ZonedDateTime.now().toString())
                        .build()
        );
    }
    @ExceptionHandler(VerificationTokenException.class)
    public ResponseEntity<ExceptionResponse> handleVerificationException(VerificationTokenException ex,
                                                                         HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .path(request.getRequestURI())
                        .timestamp(java.time.ZonedDateTime.now().toString())
                        .build()
        );
    }
    @ExceptionHandler(SignupException.class)
    public ResponseEntity<ExceptionResponse> handleSignupException(SignupException ex ,
                                                                   HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .path(request.getRequestURI())
                        .timestamp(java.time.ZonedDateTime.now().toString())
                        .build()
        );
    }
}
