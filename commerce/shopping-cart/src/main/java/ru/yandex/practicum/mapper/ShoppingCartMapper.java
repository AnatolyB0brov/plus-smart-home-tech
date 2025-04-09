package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.cart.ShoppingCartDto;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);
}
