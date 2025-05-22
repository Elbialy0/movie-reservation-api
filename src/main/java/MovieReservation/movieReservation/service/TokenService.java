package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.exceptions.VerificationTokenException;
import MovieReservation.movieReservation.model.Token;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.TokenRepo;
import MovieReservation.movieReservation.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final TokenRepo tokenRepo;
    private final UserRepo userRepo;

    public String createToken(String username) {
        String token;
        while (true){
            String randomToken = generateToken();
            if(tokenRepo.findByToken(randomToken)==null){
                token = randomToken;
                break;
            }
        }

        User user = userRepo.getByUsername(username);
        tokenRepo.save(Token.builder().token(token).user(user).expirationDate(LocalDateTime.now().plusMinutes(5)) .build());
        return token;
    }

    private String generateToken() {
        List<Integer> numbers = List.of(1,2,3,4,5,6,7,8,9,0);
        StringBuilder token = new StringBuilder();
        for(int i = 0 ; i< 8 ;i++){
            int randomIndex = (int) (Math.random() * numbers.size());
            token.append(numbers.get(randomIndex));
        }
        return token.toString();
    }


    public Token verify(String token) {
        Token dbToken = tokenRepo.findByToken(token);
        log.info(token);
        if(dbToken == null){
            throw new VerificationTokenException("Token not found. Please try again or sign up again.");
        }
        if(dbToken.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new VerificationTokenException("Token expired");
        }
        return dbToken;
    }

    public String createForgetToken(User user) {
        String token = java.util.UUID.randomUUID().toString();
        tokenRepo.save(Token.builder().token(token).user(user).expirationDate(LocalDateTime.now().plusMinutes(15)).build());
        return token;
    }
}
