package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.model.Token;
import MovieReservation.movieReservation.model.User;
import MovieReservation.movieReservation.repository.TokenRepo;
import MovieReservation.movieReservation.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
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
        if(dbToken == null){
            throw new RuntimeException("Invalid token");
        }
        if(dbToken.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }
        return dbToken;
    }
}
