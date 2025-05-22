package MovieReservation.movieReservation.exceptions;


import org.springframework.security.authentication.BadCredentialsException;

import javax.naming.AuthenticationException;

public class InvalidJwtToken extends AuthenticationException {
    public InvalidJwtToken(String message) {
        super(message);
    }
}
