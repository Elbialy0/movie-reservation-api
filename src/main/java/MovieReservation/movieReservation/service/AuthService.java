package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.SignupRequest;
import MovieReservation.movieReservation.exceptions.UsernameNotFoundException;
import MovieReservation.movieReservation.model.Role;
import MovieReservation.movieReservation.model.Token;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.RoleRepo;
import MovieReservation.movieReservation.repository.UserRepo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    public void signup(SignupRequest request) {
        if(userRepo.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Username is already taken");
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
        emailService.sendEmail(token,request.getUsername(),"Movie Reservation Account Activation",
                "activate_account.html",String.format("http://localhost:8080/api/v1/auth/activation/%s", token)

        );
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
        emailService.sendEmail(String.format("http://localhost:8080/api/v1/auth/reset-password/%s", token),email,"Movie Reservation Password Reset",
                "activate_account.html","");

    }
}
