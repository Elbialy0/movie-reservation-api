package MovieReservation.movieReservation.security;

import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.RoleRepo;
import MovieReservation.movieReservation.repository.UserRepo;
import MovieReservation.movieReservation.service.CustomUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // Extract user information
        String name = oauthUser.getAttribute("name");
        String email = oauthUser.getAttribute("email");

        if(userRepo.findByUsername(email).isEmpty()){
            userRepo.save(User.builder().username(email).firstName(name)
                    .roles(List.of(roleRepo.findById(1L).get())).enabled(true).build());
        }
        String token = jwtService.generateJwtToken(User.builder()
                .firstName(name).username(email).roles(List.of(roleRepo.findById(1L).get())).build());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write(Map.of("token", token).toString());



    }
}
