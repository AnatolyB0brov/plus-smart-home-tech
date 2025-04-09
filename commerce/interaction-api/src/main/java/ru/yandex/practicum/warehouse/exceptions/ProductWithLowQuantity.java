package ru.yandex.practicum.warehouse.exceptions;

public class ProductWithLowQuantity extends RuntimeException {
    public ProductWithLowQuantity(String message) {
        super(message);
    }
}
