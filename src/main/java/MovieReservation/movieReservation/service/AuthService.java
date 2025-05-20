package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.SignupRequest;
import MovieReservation.movieReservation.model.Role;
import MovieReservation.movieReservation.model.Token;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.RoleRepo;
import MovieReservation.movieReservation.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
                "activate_account.html","http://localhost:8080/api/v1/auth/activation"
        );

    }

    public Optional<User> getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User ?
                Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()) : Optional.empty();

    }
}
