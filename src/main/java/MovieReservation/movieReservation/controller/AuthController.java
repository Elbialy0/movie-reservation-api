package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.*;
import MovieReservation.movieReservation.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Target;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Validated SignupRequest request){
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User signup successfully");
    }
    @GetMapping("/activation/{token}")
    public ResponseEntity<String> activation(@PathVariable String token){
        authService.activate(token);
        return ResponseEntity.status(HttpStatus.OK).body("User account activated successfully");
    }
    @PostMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody @Valid ForgetPasswordRequest request){
        authService.forgetPassword(request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("Password reset link sent to your email");
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request){
        authService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String jwt,@RequestBody String  token){
        authService.logout(jwt, token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody String refreshToken){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authService.refresh(refreshToken));

    }





}
