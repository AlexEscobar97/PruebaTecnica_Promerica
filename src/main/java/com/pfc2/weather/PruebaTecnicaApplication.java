package com.pfc2.weather;

import com.pfc2.weather.security.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class PruebaTecnicaApplication {

	private static final Logger logger = LoggerFactory.getLogger(PruebaTecnicaApplication.class);

	public static void main(String[] args) {
		logger.info("API PruebaTecnicaApplication Banco Promerica: Inicio");
		SpringApplication.run(PruebaTecnicaApplication.class, args);
	}

}
