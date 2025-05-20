package MovieReservation.movieReservation.controller;

import MovieReservation.movieReservation.dto.SignupRequest;
import MovieReservation.movieReservation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Validated SignupRequest request){
        authService.signup(request);
        return ResponseEntity.accepted().body("User signup successfully");
    }
    @GetMapping("/activation/{token}")
    public ResponseEntity<String> activation(@PathVariable String token){
        authService.activate(token);
        return ResponseEntity.accepted().body("User account activated successfully");
    }


}
