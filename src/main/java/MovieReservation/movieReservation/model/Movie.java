package MovieReservation.movieReservation.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String poster;
    private String description;
    private String trailer;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    @CreatedDate
    @Column(updatable = false,nullable = false)
    private LocalDate createdDate;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate lastModifiedDate;
    @LastModifiedBy
    private Integer lastModifiedBy;
    @OneToMany(mappedBy = "movie")
    private List<ShowTime> showTimes;
    private Boolean isAvailable;

    @OneToMany(mappedBy = "movie")
    private List<Feedback> feedbacks;

    public int getAverageRate(){
        int sum = 0;
        for (Feedback feedback : feedbacks) {
            sum += feedback.getRate();
        }
        return sum / feedbacks.size();
    }
}
