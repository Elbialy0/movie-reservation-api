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
    private String clientId = "AZTLzSsoc233Mq0u785Rh0HYmZ3tWRNNV8yLNjc6dyKngPDtSxPQQkHvS7LZ11w8lj-SH4YygdxdFAPK";
    private String clientSecret = "EE_z756c7UuFiZujuN2KDVxT54-zo74iKdbzhVxw29KsPqsTsxIlA8c04MNW_gJOL8OWMxKiCvtKN9TqEE_z756c7UuFiZujuN2KDVxT54-zo74iKdbzhVxw29KsPqsTsxIlA8c04MNW_gJOL8OWMxKiCvtKN9Tq";
    private String mode = "sandbox";

    @Bean
    public APIContext apiContext(){
        return new APIContext(clientId,clientSecret,mode);
    }

}
