package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.BookingMapper;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.Booking;
import ru.yandex.practicum.model.Warehouse;
import ru.yandex.practicum.repository.BookingRepository;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.cart.BookedProductsDto;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.dto.AddressDto;
import ru.yandex.practicum.warehouse.dto.AssemblyProductForOrderFromShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.NewProductInWarehouseRequestDto;
import ru.yandex.practicum.warehouse.exceptions.ProductWithLowQuantity;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private static final AddressDto[] ADDRESSES =
            new AddressDto[]{
                    AddressDto.builder()
                            .country("Russia")
                            .city("ADDRESS_1")
                            .street("Liteinaya")
                            .house("3")
                            .flat("1")
                            .build(),
                    AddressDto.builder()
                            .country("Russia")
                            .city("ADDRESS_2")
                            .street("Leningradskiy pr")
                            .house("5")
                            .flat("2")
                            .build()};


    @Override
    public void newProductToWarehouse(NewProductInWarehouseRequestDto requestDto) {
        warehouseRepository.findById(requestDto.getProductId()).ifPresent(warehouse -> {
            throw new SpecifiedProductAlreadyInWarehouseException("Product already exists in warehouse");
        });
        Warehouse warehouse = warehouseMapper.toWarehouse(requestDto);
        warehouseRepository.save(warehouse);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest requestDto) {
        Warehouse warehouse = warehouseRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new NoSpecifiedProductInWarehouseException("Product with id " + requestDto.getProductId() + " not found")
        );
        warehouse.setQuantity(warehouse.getQuantity() + requestDto.getQuantity());
    }
    //Так хотели по ТЗ
    @Override
    public AddressDto getAddress() {
        return ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];
    }

    @Override
    public BookedProductsDto assemblyProductForOrder(AssemblyProductForOrderFromShoppingCartDto assemblyProductDto) {
        Booking booking = bookingRepository.findById(assemblyProductDto.getShoppingCartId()).orElseThrow(
                () -> new RuntimeException("Shopping cart" + assemblyProductDto.getShoppingCartId() + " not found"));

        Map<UUID, Long> productsInBooking = booking.getProducts();
        List<Warehouse> productsInWarehouse = warehouseRepository.findAllById(productsInBooking.keySet());
        productsInWarehouse.forEach(warehouse -> {
            if (warehouse.getQuantity() < productsInBooking.get(warehouse.getProductId())) {
                throw new ProductWithLowQuantity(
                        "Product " + warehouse.getProductId() + "is sold out");
            }
        });

        for (Warehouse warehouse : productsInWarehouse) {
            warehouse.setQuantity(warehouse.getQuantity() - productsInBooking.get(warehouse.getProductId()));
        }
        booking.setOrderId(assemblyProductDto.getOrderId());
        return bookingMapper.toBookedProductsDto(booking);
    }

    @Override
    public BookedProductsDto bookProduct(ShoppingCartDto shoppingCartDto) {
        Map<UUID, Long> products = shoppingCartDto.getProducts();
        List<Warehouse> productsInWarehouse = warehouseRepository.findAllById(products.keySet());
        productsInWarehouse.forEach(warehouse -> {
            if (warehouse.getQuantity() < products.get(warehouse.getProductId())) {
                throw new ProductWithLowQuantity(
                        "Product " + warehouse.getProductId() + "is sold out");
            }
        });

        double deliveryVolume = productsInWarehouse.stream()
                .map(v -> v.getDimension().getDepth() * v.getDimension().getWidth()
                        * v.getDimension().getHeight())
                .mapToDouble(Double::doubleValue)
                .sum();

        double deliveryWeight = productsInWarehouse.stream()
                .map(Warehouse::getWeight)
                .mapToDouble(Double::doubleValue)
                .sum();

        boolean fragile = productsInWarehouse.stream()
                .anyMatch(Warehouse::isFragile);


        Booking newBooking = Booking.builder()
                .shoppingCartId(shoppingCartDto.getShoppingCartId())
                .deliveryVolume(deliveryVolume)
                .deliveryWeight(deliveryWeight)
                .fragile(fragile)
                .products(products)
                .build();
        Booking booking = bookingRepository.save(newBooking);
        return bookingMapper.toBookedProductsDto(booking);
    }

    @Override
    public void returnProductToWarehouse(Map<UUID, Long> products) {
        List<Warehouse> warehousesItems = warehouseRepository.findAllById(products.keySet());
        for (Warehouse warehouse : warehousesItems) {
            warehouse.setQuantity(warehouse.getQuantity() + products.get(warehouse.getProductId()));
        }
    }
}
