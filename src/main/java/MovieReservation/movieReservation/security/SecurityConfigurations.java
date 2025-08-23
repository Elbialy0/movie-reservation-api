package MovieReservation.movieReservation.security;

import MovieReservation.movieReservation.exceptions.CustomAuthenticationEntryPoint;
import MovieReservation.movieReservation.filters.CsrfCookieFilter;
import MovieReservation.movieReservation.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    private final AuthenticationProvider authenticationProvider;
    private final Oauth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint;
    private final CustomOAuth2AuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtFilter jwtFilter;



    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();

        http.csrf(csrfConfig->csrfConfig.csrfTokenRequestHandler(csrfHandler)
                        .ignoringRequestMatchers("/auth/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/auth/logout").authenticated().
                        requestMatchers( "/movie/add",
                                "/movie/activate/*",
                                "/movie/poster"
                                ,"/movie/tailer",
                                "/reservation/valid",
                                "/showTime/new",
                                "/showTime/update/*").hasRole("ADMIN").
                        requestMatchers(
                                "/auth/**",
                                "/error",
                                "/oauth2/authorization/google",
                                "/showTime/filter",
                                "/payment/sucess",
                                "/movie/filter",
                                "/showTime/all",
                                "/showTime/findMovie",
                                "/movie/available",
                                "/movie/rate/*"
                        ).permitAll().anyRequest().authenticated())

                .authenticationProvider(authenticationProvider)
                .oauth2Login(auth->auth.successHandler(successHandler))
                .exceptionHandling(eh->eh.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }

}
