package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.delivery.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "deliveries")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID deliveryId;
    @ManyToOne
    @JoinColumn(name = "from_address_id", referencedColumnName = "address_id")
    Address fromAddress;
    @ManyToOne
    @JoinColumn(name = "to_address_id", referencedColumnName = "address_id")
    Address toAddress;
    UUID orderId;
    @Enumerated(EnumType.STRING)
    DeliveryState deliveryState;
}
