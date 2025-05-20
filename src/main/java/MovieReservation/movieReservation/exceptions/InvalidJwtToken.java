package MovieReservation.movieReservation.exceptions;

import io.jsonwebtoken.ExpiredJwtException;

public class InvalidJwtToken extends RuntimeException  {
    public InvalidJwtToken(String message) {
        super(message);
    }
}
