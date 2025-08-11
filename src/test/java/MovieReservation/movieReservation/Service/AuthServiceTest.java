package MovieReservation.movieReservation.Service;
import MovieReservation.movieReservation.dto.SignupRequest;
import MovieReservation.movieReservation.exceptions.SignupException;
import MovieReservation.movieReservation.model.Role;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.RoleRepo;
import MovieReservation.movieReservation.repository.UserRepo;
import MovieReservation.movieReservation.service.AuthService;
import MovieReservation.movieReservation.service.EmailService;
import MovieReservation.movieReservation.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    RoleRepo roleRepo;
    @Mock
    TokenService tokenService;
    @Mock
    EmailService emailService;
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepo userRepo;
    @InjectMocks
    AuthService authService;
    User dummy = User.builder()
            .id(1L)
            .username("john@mail.com")
            .password("encoded-pass")
            .firstName("John")
            .lastName("Doe")
            .build();
    SignupRequest request = SignupRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .username("john@mail.com")
            .password("plain-pass-123")
            .phoneNumber("+10000000000")
            .birthDate(LocalDate.of(1990,1,1))
            .build();
    
    @Test void throwExceptionWhenRegisterUserIfUserAlreadyRegistered(){

     when(userRepo.findByUsername("john@mail.com")).thenReturn(Optional.of(dummy));
        Assertions.assertThrows(SignupException.class, () -> authService.signup(request));
    }

    @Test void doesNotThrowExceptionWhenRegisterUserIfUserNotRegistered(){
        when(userRepo.findByUsername("john@mail.com")).thenReturn(Optional.empty());
        when(roleRepo.findById(1L)).thenReturn(Optional.of(new Role(1L, "USER", null)));
        Assertions.assertDoesNotThrow(()->authService.signup(request));
    }

    @Test void throwExceptionWhenRegisterUserIfRoleEqualsNull(){
        when(userRepo.findByUsername("john@mail.com")).thenReturn(Optional.empty());
        when(roleRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,()->authService.signup(request));

    }
    @Test void giveUserWithThrowingExceptionWhenSendingTheActivationEmailThenUserRegistered(){
        when(userRepo.findByUsername("john@mail.com")).thenReturn(Optional.empty());
        when(roleRepo.findById(1L)).thenReturn(Optional.of(new Role(1L, "USER", null)));
        when(tokenService.createToken("john@mail.com")).thenReturn("token");

        doThrow(RuntimeException.class)
                .when(emailService)
                .sendEmailForActivation(anyString(), anyString(), any(), anyString(), anyString());

        Assertions.assertDoesNotThrow(()->authService.signup(request));




    }





}
