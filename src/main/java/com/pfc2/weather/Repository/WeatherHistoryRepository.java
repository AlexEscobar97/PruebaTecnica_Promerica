package com.pfc2.weather.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pfc2.weather.Model.H2base.WeatherHistory;
import java.util.List;

public interface WeatherHistoryRepository extends JpaRepository<WeatherHistory, Long> {
    List<WeatherHistory> findByLatAndLon(Double lat, Double lon);
}
