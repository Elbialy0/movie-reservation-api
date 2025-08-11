package MovieReservation.movieReservation;

import MovieReservation.movieReservation.config.ProjectConfigurations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(ProjectConfigurations.class)

class MovieReservationApplicationTests {

	@Test
	void contextLoads() {
	}


}
