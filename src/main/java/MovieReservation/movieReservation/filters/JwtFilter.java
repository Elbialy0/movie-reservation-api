package MovieReservation.movieReservation.filters;

import MovieReservation.movieReservation.exceptions.InvalidJwtToken;
import MovieReservation.movieReservation.security.JwtService;
import MovieReservation.movieReservation.service.AuthService;
import MovieReservation.movieReservation.service.JwtBlackListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtBlackListService jwtBlackListService;


    @SneakyThrows
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        if(!request.getRequestURI().equals("/api/v1/auth/logout")&&request.getRequestURI().startsWith("/api/v1/auth")){
            filterChain.doFilter(request, response);
            return;
        }
        if(jwtBlackListService.isTokenBlacklisted(request.getHeader("Authorization"))){
            throw new InvalidJwtToken("Token is blacklisted");

        }
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authorizationHeader.substring(7);
        if(SecurityContextHolder.getContext().getAuthentication()==null){
            // validate the token
              Authentication authentication =  jwtService.validate(token);
              if(authentication != null) {
                  // save the authentication in the context holder
                  SecurityContextHolder.getContext().setAuthentication(authentication);
              }

        }

        filterChain.doFilter(request, response);
    }


}
