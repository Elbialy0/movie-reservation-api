package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.*;
import MovieReservation.movieReservation.exceptions.SignupException;
import MovieReservation.movieReservation.exceptions.UsernameNotFoundException;
import MovieReservation.movieReservation.model.Role;
import MovieReservation.movieReservation.model.Token;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.RoleRepo;
import MovieReservation.movieReservation.repository.TokenRepo;
import MovieReservation.movieReservation.repository.UserRepo;
import MovieReservation.movieReservation.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepo tokenRepo;
    private final JwtBlackListService jwtBlackListService;


    public void signup(SignupRequest request) {
        if(userRepo.findByUsername(request.getUsername()).isPresent()){
            throw new SignupException("Username is already taken");
        }
        Role role = roleRepo.findById(1L).orElseThrow(() -> new RuntimeException("Role not found"));
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .phoneNumber(request.getPhoneNumber())
                .roles(List.of(role))
                .build();
        userRepo.save(user);
        String  token = tokenService.createToken(request.getUsername());
        try {
            emailService.sendEmailForActivation(token, request.getUsername(), "Movie Reservation Account Activation",
                    "activate_account.html", String.format("http://localhost:8080/api/v1/auth/activation/%s", token)

            );
        }catch (Exception ex){
            log.error("Can't send mail i will try again");
        }
        log.info(String.format("http://localhost:8080/api/v1/auth/activation/%s", token));

    }

    public Optional<User> getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User ?
                Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()) : Optional.empty();

    }

    public void activate(String token) {
        Token dbToken = tokenService.verify(token);
        User user = dbToken.getUser();
        user.setEnabled(true);
        userRepo.save(user);
    }

    public void   forgetPassword(String email) {
        if(!userRepo.findByUsername(email).isPresent()){
            throw new UsernameNotFoundException("Username not found");
        }
        User user = userRepo.getByUsername(email);
        String token = tokenService.createForgetToken(user);
        emailService.sendEmailForForgetPassword(String.format("http://localhost:8080/api/v1/auth/reset-password/%s", token),email,"Movie Reservation Password Reset",
                "reset_password.html");

    }

    public void resetPassword( ResetPasswordRequest request) {
        Token dbToken = tokenService.verify(request.getToken()); // verify the token
        User user = dbToken.getUser(); // get the user associated with the token
        user.setPassword(passwordEncoder.encode(request.getPassword())); // update the password
        userRepo.save(user); // save the user with the new password
    }


    public LoginResponse login(@Valid LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername()
                ,request.getPassword())
        ); // validate the user using the authentication manager by the default provided in spring security
        User user = (User) authentication.getPrincipal(); // get the user from the authentication object
        String jwt = jwtService.generateJwtToken(user); // generate the jwt token for the user
        String refreshToken = java.util.UUID.randomUUID().toString(); // generate a random refresh token for the user
        tokenRepo.save(Token.builder().token(refreshToken).user(user).expirationDate(java.time.LocalDateTime.now().plusDays(7)).build()); // save the refresh token in the database with a 7 day expiration date

        return LoginResponse.builder().jwtToken(jwt).refreshToken(refreshToken).build(); // return the jwt token and refresh token in the response object

    }

    public void logout(String jwt,String token) {

        Token refreshToken = tokenService.verify(token);// find the refresh token in the database
        tokenRepo.delete(refreshToken);// delete the refresh token from the database if it exists
        jwtBlackListService.blacklistToken(jwt,864000); // blacklist the jwt token for 24 hours from now using the jwt blacklist service
        SecurityContextHolder.getContext().setAuthentication(null);// clear the authentication context if the user is logged out successfully
        log.info("User logged out successfully"); // log the user logout successfully in the console
    }

    public LoginResponse refresh(String refreshToken) {
        Token dbToken = tokenService.verify(refreshToken);
        User user = dbToken.getUser();
        return LoginResponse.builder().jwtToken(jwtService.generateJwtToken(user)).refreshToken(refreshToken).build();
    }
}
