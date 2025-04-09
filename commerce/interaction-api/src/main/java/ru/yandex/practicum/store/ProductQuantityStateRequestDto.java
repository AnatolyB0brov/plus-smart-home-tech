package ru.yandex.practicum.store;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.store.enums.QuantityState;

import java.util.UUID;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductQuantityStateRequestDto {
    @NotNull
    UUID productId;
    @NotNull
    QuantityState quantityState;
}
