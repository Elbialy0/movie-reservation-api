package MovieReservation.movieReservation.config;

import com.paypal.base.rest.APIContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@RequiredArgsConstructor
public class PaypalConfiguration {
    private String clientId = "AZ0oqCMU_6fcJUP9HoHbZNTHiY0bKf7luhMxKmxsqyY0V8E8iOSd3HVY4ezpBlQGWhKueX_AwBZfLBJg";
    private String clientSecret = "EF2SB2k5VxnaMPZy1XZSSn-txOjho524t7MWaSh5_ydtyS-PzkoeYnAhlw8af5SvzTLrG0IoCtxkJ-RT";
    private String mode = "sandbox";

    @Bean
    public APIContext apiContext(){
        return new APIContext(clientId,clientSecret,mode);
    }

}
