package ru.yandex.practicum.service;

import ru.yandex.practicum.store.PageableDto;
import ru.yandex.practicum.store.ProductDto;
import ru.yandex.practicum.store.ProductQuantityStateRequestDto;
import ru.yandex.practicum.store.enums.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductDto> getProductsByCategory(ProductCategory productCategory, PageableDto pageableDto);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(UUID productId);

    boolean updateQuantityState(ProductQuantityStateRequestDto productQuantityStateRequestDto);

    ProductDto getProduct(UUID productId);
}
