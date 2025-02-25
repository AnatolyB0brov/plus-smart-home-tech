package ru.yandex.practicum.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, WebRequest webRequest) {
        String body = webRequest.getParameterMap().keySet().iterator().next();
        log.error("HANDLER {} {}",body, e.getMessage());
    }
}
