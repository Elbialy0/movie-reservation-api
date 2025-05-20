package MovieReservation.movieReservation.security;

import MovieReservation.movieReservation.exceptions.InvalidJwtToken;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.service.CustomUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${spring.application.security.jwt.expiration}")
    private Long EXPIRATION_TIME;
    @Value("${spring.application.security.jwt.secret-key}")
    private String SECRET;
    private final CustomUserService customUserService;

    public String generateJwtToken(User user){
        String username = user.getUsername();
        var authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return buildToken(username,authorities);

    }

    private String buildToken(String username, List<String> authorities) {
        return Jwts.builder()
                .subject(username)
                .claim("authorities",authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Authentication validate(String token) {
        try {
            // Validate the token and extract the claims
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            // Check the expiration
            if(claims.getExpiration().before(new Date())){
                // todo InvalidJwtTokenException
                throw new InvalidJwtToken("Token expired");
            }

            // Check the authorities were existed or not
            if(claims.get("authorities", List.class) == null){
                throw new InvalidJwtToken("Invalid token");
            }
            // Extract username
            var username = claims.getSubject();
            User user = (User) customUserService.loadUserByUsername(username);

            // Extract authorities
            @SuppressWarnings("unchecked")
            List<String> rawAuthorities = claims.get("authorities", List.class);
            if (rawAuthorities == null) throw new InvalidJwtToken("Invalid token");

            var authorities = rawAuthorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();


            // Generate the Authentication token
            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtToken("Token expired");
        }
        catch (Exception e){
            throw new InvalidJwtToken("Invalid token");
        }


    }
}
