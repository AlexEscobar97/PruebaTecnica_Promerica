package com.pfc2.weather.Model.H2base;


import com.pfc2.weather.Model.Weather;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "weather_history")
public class WeatherHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lon;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String weather; // Representación JSON de Weather

    @Column(nullable = false)
    private Double tempMin;

    @Column(nullable = false)
    private Double tempMax;

    @Column(nullable = false)
    private int humidity;

    @Column(nullable = false)
    private LocalDateTime created;

    // Constructor, getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "WeatherHistory{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", weather='" + weather + '\'' +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", humidity=" + humidity +
                ", created=" + created +
                '}';
    }
}
