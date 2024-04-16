package com.pfc2.weather.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SystemStatusController {

    private static final Logger logger = LoggerFactory.getLogger(SystemStatusController.class);

    @GetMapping("/status")
    public HttpStatus getSystemStatus() {
        logger.info("EL SERVICIO NO REQUIERE AUTORIZACION");
        // Retorna código 200 OK
        return HttpStatus.OK;
    }
}