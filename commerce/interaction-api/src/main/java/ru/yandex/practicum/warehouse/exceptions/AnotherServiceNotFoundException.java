package ru.yandex.practicum.warehouse.exceptions;

public class AnotherServiceNotFoundException extends RuntimeException {
    public AnotherServiceNotFoundException(String message) {
        super(message);
    }
}
