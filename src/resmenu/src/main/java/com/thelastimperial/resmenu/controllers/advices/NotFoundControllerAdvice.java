package com.thelastimperial.resmenu.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class NotFoundControllerAdvice {
    @ExceptionHandler(ResponseStatusException.class)
    public String handler404(ResponseStatusException ex){
        String template = "/errors/404";
        if(ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)){
            template = "/errors/400";
        }
        return template;
    }
}
