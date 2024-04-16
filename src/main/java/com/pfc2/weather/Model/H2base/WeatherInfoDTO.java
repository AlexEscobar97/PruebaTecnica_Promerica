package com.pfc2.weather.Model.H2base;

public class WeatherInfoDTO {
    private String weather;
    private Double tempMin;
    private Double tempMax;
    private int humidity;

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
}
