package MovieReservation.movieReservation.security;

import MovieReservation.movieReservation.filters.JwtFilter;
import MovieReservation.movieReservation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    private final AuthenticationProvider authenticationProvider;
    private final Oauth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint;
    private final CustomOAuth2AuthenticationSuccessHandler successHandler;
    private final JwtService jwtService;
    private final AuthService authService;

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtService, authService);
    }
    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->
                        auth.requestMatchers(
                                "/api/v1/auth/**",
                                "/error","/oauth2/authorization/google"

                        ).permitAll().anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .oauth2Login(auth->auth.successHandler(successHandler))
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(oauth2AuthenticationEntryPoint))
                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
