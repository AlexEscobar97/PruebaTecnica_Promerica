package com.pfc2.weather.Controller;

import com.pfc2.weather.Controller.Excepcions.GlobalExceptionHandler;
import com.pfc2.weather.Model.H2base.WeatherHistory;
import com.pfc2.weather.Model.H2base.WeatherInfoDTO;
import com.pfc2.weather.Model.WeatherData;
import com.pfc2.weather.Model.WeatherRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.pfc2.weather.Repository.WeatherHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherControllerDos {

    private static final Logger logger = LoggerFactory.getLogger(WeatherControllerDos.class);

    @Value("${openweathermap.api.key}")
    private String apiKey;
    private static final String OPEN_WEATHER_MAP_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Autowired
    private WeatherHistoryRepository weatherHistoryRepository;

    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    /*@GetMapping("/sys/status")
    public HttpStatus checkSystemStatus() {
        // Retorna código de estado HTTP 200
        return HttpStatus.OK;
    }*/

    @GetMapping("/history")
    public ResponseEntity<List<WeatherHistory>> getWeatherHistory() {
        List<WeatherHistory> weatherHistoryList = weatherHistoryRepository.findAll();
        logger.info("Lista  "+ weatherHistoryList.size()+": "+weatherHistoryList.toString());
        return ResponseEntity.ok(weatherHistoryList);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getWeather(@RequestBody WeatherRequest request) {
        // Verificar si los valores lat y lon son proporcionados
        logger.info("Valores de entrada");
        logger.info(request.toString());
        System.out.println("Entrada: "+ request.toString());
        if (Double.isNaN(request.getLat()) || Double.isNaN(request.getLon()))  {
            throw exceptionHandler.new BadRequestException("Los campos 'lat' y 'lon' son requeridos");
        }

        // Obtener la fecha y hora actual
        LocalDateTime currentTime = LocalDateTime.now();

        // Buscar en la base de datos si existe un registro para estas coordenadas y si fue registrado hace menos de 10 minutos
        List<WeatherHistory> optionalWeatherHistory = weatherHistoryRepository.findByLatAndLon(request.getLat(), request.getLon());
        logger.info("Lista: "+optionalWeatherHistory.toString());

        // Verificar si la lista no está vacía y si el primer elemento no es nulo
        if (optionalWeatherHistory != null && !optionalWeatherHistory.isEmpty()) {
            WeatherHistory weatherHistory = optionalWeatherHistory.get(0);
            System.out.println("weatherHistory: "+ weatherHistory.toString());
            // Calcular la diferencia en minutos entre la fecha de creación y la fecha actual
            long minutesDifference = java.time.Duration.between(weatherHistory.getCreated(), currentTime).toMinutes();
            System.out.println("Minutos: "+ minutesDifference +" ingreso"+(minutesDifference <= 10));
            if (minutesDifference <= 10) {
                // Si la diferencia es menor o igual a 10 minutos, retornar el valor desde la base de datos
                WeatherInfoDTO weatherInfoDTO = new WeatherInfoDTO();
                weatherInfoDTO.setWeather(weatherHistory.getWeather());
                weatherInfoDTO.setTempMin(weatherHistory.getTempMin());
                weatherInfoDTO.setTempMax(weatherHistory.getTempMax());
                weatherInfoDTO.setHumidity(weatherHistory.getHumidity());
                return ResponseEntity.ok(weatherInfoDTO);
            }
        }

        // Si no se encuentra en la base de datos o la diferencia es mayor a 10 minutos, consultar el API del tercero
        String url = OPEN_WEATHER_MAP_API_URL + "?lat=" + request.getLat() + "&lon=" + request.getLon() + "&appid=" + apiKey;

        try {
            RestTemplate restTemplate = new RestTemplate();
            WeatherData weatherData = restTemplate.getForObject(url, WeatherData.class);

            // Guardar o actualizar el registro en la base de datos
            WeatherHistory newWeatherHistory = new WeatherHistory();
            newWeatherHistory.setLat(request.getLat());
            newWeatherHistory.setLon(request.getLon());
            newWeatherHistory.setWeather(weatherData.getWeather().toString());
            newWeatherHistory.setTempMin(weatherData.getMain().getTemp_min());
            newWeatherHistory.setTempMax(weatherData.getMain().getTemp_max());
            newWeatherHistory.setHumidity(weatherData.getMain().getHumidity());
            newWeatherHistory.setCreated(currentTime);
            System.out.println("Guardar: "+ newWeatherHistory.toString());

            weatherHistoryRepository.save(newWeatherHistory);

            // Retornar la respuesta de la API del tercero
            return ResponseEntity.ok(weatherData);
        } catch (HttpClientErrorException.BadRequest ex) {
            // Capturar el error 400 Bad Request de la API de OpenWeatherMap
            throw new GlobalExceptionHandler.NotFoundException("No se encontraron condiciones climáticas para las coordenadas especificadas");
        } catch (HttpClientErrorException.Unauthorized ex) {
            // Capturar el error 401 Unauthorized de la API de OpenWeatherMap
            throw exceptionHandler.new UnauthorizedException("Solicitud sin autorización");
        } catch (HttpClientErrorException.NotFound ex) {
            // Capturar el error 404 Not Found de la API de OpenWeatherMap
            throw  new GlobalExceptionHandler.NotFoundException("No se encontraron condiciones climáticas para las coordenadas especificadas");
        } catch (HttpServerErrorException ex) {
            // Capturar errores 500 Server Error de la API de OpenWeatherMap
            throw new GlobalExceptionHandler.ServerException("Error en el servidor de OpenWeatherMap");
        }
    }


}