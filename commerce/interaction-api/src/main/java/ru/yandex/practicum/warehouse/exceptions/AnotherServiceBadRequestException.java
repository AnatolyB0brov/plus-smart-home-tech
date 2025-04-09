package ru.yandex.practicum.warehouse.exceptions;

public class AnotherServiceBadRequestException extends RuntimeException {
    public AnotherServiceBadRequestException(String message) {
        super(message);
    }
}
