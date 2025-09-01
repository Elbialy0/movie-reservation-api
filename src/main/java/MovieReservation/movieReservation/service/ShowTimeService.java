package MovieReservation.movieReservation.service;

import MovieReservation.movieReservation.dto.PageResponse;
import MovieReservation.movieReservation.dto.ShowTimeRequest;
import MovieReservation.movieReservation.dto.ShowTimeResponse;
import MovieReservation.movieReservation.mapper.Mapper;
import MovieReservation.movieReservation.model.ShowTime;
import MovieReservation.movieReservation.repository.HallRepo;
import MovieReservation.movieReservation.repository.MovieRepo;
import MovieReservation.movieReservation.repository.ShowTimeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowTimeService {
    private final ShowTimeRepo showTimeRepo;
    private final HallRepo hallRepo;
    private final EmailService emailService;
    private final MovieRepo movieRepo;
    private final Mapper mapper;
    private final CacheManager cacheManager;



    @Cacheable(value = "idempotentCache", key = "#idempotencyKey")
    public ShowTimeResponse createNewShowTime(ShowTimeRequest request,String idempotencyKey) {
      ShowTime showTime = showTimeRepo.save(ShowTime.builder()
              .time(request.getTime())
              .reservations(new ArrayList<>()).price(request.getPrice()
        ).movie(movieRepo.findById((long) request.getMovieId()).orElseThrow(()
        ->new RuntimeException("Movie not found")))
              .hall(hallRepo.findById((long)request.getHallId()).orElseThrow(
                ()-> new RuntimeException("Hall not found")
        )).price(request.getPrice()).build());
      emailService
              .sendEmailForAll("Add new show","New movie you can watch :| "
                      ,"show_time.html","http://localhost:8080/api/v1/auth/reservation/reserve",showTime);


        Cache cache = cacheManager.getCache("SHOWTIME_CACHE");
        if(cache!=null) {
            cache.put(showTime.getId(), mapper.mapToShowTimeResponse(showTime));
        }

      return mapper.mapToShowTimeResponse(showTime);

    }

    public ShowTimeResponse updateShow(ShowTimeRequest request, long id) {
        ShowTime showTime = showTimeRepo.findById(id).orElseThrow(
                ()-> new RuntimeException("Show time not found")
        );
        showTime.setTime(request.getTime());
        showTime.setHall(hallRepo.findById((long)request.getHallId()).orElseThrow(
                ()-> new RuntimeException("Hall not found")
        ));
        showTime.setMovie(movieRepo.findById((long) request.getMovieId()).orElseThrow(
                ()-> new RuntimeException("Movie not found")
        ));
        showTime.setPrice(request.getPrice());
        showTimeRepo.save(showTime);
        log.info("Show time updated successfully");

        Cache cache = cacheManager.getCache("SHOWTIME_CACHE");
        if(cache!=null) {
            cache.put(showTime.getId(), mapper.mapToShowTimeResponse(showTime));
        }
       return mapper.mapToShowTimeResponse(showTime);

    }


    @PreAuthorize("hasRole('ADMIN')")
    public void deleteShow(long id) {
        ShowTime showTime = showTimeRepo.findById(id).orElseThrow(
                ()-> new RuntimeException("Show time not found")
        );
        showTimeRepo.delete(showTime);
        Cache cache = cacheManager.getCache("SHOWTIME_CACHE");
        if(cache!=null) {
            cache.evict(showTime.getId());
        }
        log.info("Show time deleted successfully");
    }

    public PageResponse<ShowTimeResponse> getByType(String genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ShowTime> showTimes = showTimeRepo.findAllByGenre(pageable,genre);
        List<ShowTimeResponse> content = showTimes.getContent().stream().map(mapper::mapToShowTimeResponse).toList();
        return new PageResponse<>(
                content,
                showTimes.getNumber(),
                showTimes.getNumberOfElements(),
                showTimes.getSize(),
                showTimes.getTotalPages(),
                showTimes.isFirst(),
                showTimes.isLast()
        );
    }

    public PageResponse<ShowTimeResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ShowTime> showTimes = showTimeRepo.findAll(pageable);
        List<ShowTimeResponse> content = showTimes.getContent().stream().map(mapper::mapToShowTimeResponse).toList();
        return new PageResponse<>(
                content,
                showTimes.getNumber(),
                showTimes.getNumberOfElements(),
                showTimes.getSize(),
                showTimes.getTotalPages(),
                showTimes.isFirst(),
                showTimes.isLast()
        );
    }

    public PageResponse<ShowTimeResponse> getByMovieTitle(String movieTitle, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ShowTime> showTimes = showTimeRepo.findByMovieTitle(pageable, movieTitle);
        List<ShowTimeResponse> content = showTimes.getContent().stream().map(mapper::mapToShowTimeResponse).toList();
        return new PageResponse<>(
                content,
                showTimes.getNumber(),
                showTimes.getNumberOfElements(),
                showTimes.getSize(),
                showTimes.getTotalPages(),
                showTimes.isFirst(),
                showTimes.isLast()
        );

    }

    public ShowTimeResponse getShowTime(long id) {
        Cache cache = cacheManager.getCache("SHOWTIME_CACHE");
        if(cache!=null) {
         ShowTimeResponse response = cache.get(id,ShowTimeResponse.class);
         if(response!=null)return response;
        }
        ShowTime showTime = showTimeRepo.findById(id).orElseThrow(()->new RuntimeException("Show time not found"));

        ShowTimeResponse response =mapper.mapToShowTimeResponse(showTime);
        if(cache!=null) {
            cache.put(id, response);
        }
        return response;

    }
}
