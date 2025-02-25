package ru.yandex.practicum.controller.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        if ("POST".equalsIgnoreCase(request.getMethod()))
        {
            String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            log.info("FILTER {}",requestBody);
        }
        // Продолжить обработку запроса
        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        // Создание записи лога
        String logMessage = String.format("request method: %s, request URI: %s, response status: %d, request processing time: %d ms",
                request.getMethod(), request.getRequestURI(), response.getStatus(), duration);

        // Запись лога
        logger.info(logMessage);
    }
}
