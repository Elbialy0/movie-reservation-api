package MovieReservation.movieReservation.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int numberOfPage;
    private int numberOfElements;
    private int size;
    private int totalPages;
    private boolean first;
    private boolean last;
}
