package MovieReservation.movieReservation.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // Extract user information
        String name = oauthUser.getAttribute("name");
        String email = oauthUser.getAttribute("email");

        // Set response status and content type
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");

        // Write user details to response body
        response.getWriter().write("{\"name\": \"" + name + "\", \"email\": \"" + email + "\"}");
    }
}
