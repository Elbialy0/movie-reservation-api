package MovieReservation.movieReservation.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Genre {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "genre")
    private List<Movie> movies;
}
