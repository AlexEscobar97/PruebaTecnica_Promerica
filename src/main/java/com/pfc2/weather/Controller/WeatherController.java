package com.pfc2.weather.Controller;

import com.pfc2.weather.Controller.Excepcions.GlobalExceptionHandler;
import com.pfc2.weather.Model.H2base.WeatherHistory;
import com.pfc2.weather.Model.WeatherData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/clima")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    /*@GetMapping(value = "/weatherPrueba1", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWeather(@RequestParam("lat") String lat, @RequestParam("lon") String lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
        System.out.println("URL:"+url);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }*/

    @Operation(summary = "Obtener clima", description = "Obtiene las condiciones climáticas actuales para una latitud y longitud específicas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se devuelven las condiciones climáticas."),
            @ApiResponse(responseCode = "404", description = "No se encontraron condiciones climáticas para las coordenadas especificadas.")
    })

    @GetMapping(value = "/weather", produces = MediaType.APPLICATION_JSON_VALUE)
    public WeatherData getWeatherJSON(@RequestParam("lat") String lat, @RequestParam("lon") String lon) throws GlobalExceptionHandler.NotFoundException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
        try {
            RestTemplate restTemplate = new RestTemplate();
            WeatherData weatherData;
            weatherData = restTemplate.getForObject(url, WeatherData.class);

            if (weatherData == null) {
                logger.error("Valor del Dato"+weatherData);
                throw new GlobalExceptionHandler.NotFoundException("No se encontraron condiciones climáticas para las coordenadas especificadas");
            }

            return weatherData;

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

    public Map<String, String> authorized(@RequestParam String code){
        return Collections.singletonMap("code", code);
    }
}
