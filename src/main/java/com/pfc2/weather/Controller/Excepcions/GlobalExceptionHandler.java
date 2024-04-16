package com.pfc2.weather.Controller.Excepcions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException ex) {
        return new ErrorResponse("400", ex.getMessage());
    }

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
        return new ErrorResponse("401", ex.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse("404", ex.getMessage());
    }

    @ExceptionHandler({ServerException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleServerException(ServerException ex) {
        return new ErrorResponse("503", ex.getMessage());
    }

    public static class ErrorResponse {
        private String code;
        private String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        // Getters y setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class ServerException extends RuntimeException {
        public ServerException(String message) {
            super(message);
        }
    }
}





