create table if not exists warehouse_product
(
    product_id uuid primary key,
    quantity   integer not null,
    fragile    boolean not null,
    width      double precision not null,
    height     double precision not null,
    depth      double precision not null,
    weight     double precision not null
);

create table if not exists bookings
(
    shopping_cart_id uuid primary key,
    delivery_weight  double precision not null,
    delivery_volume  double precision not null,
    fragile          boolean not null,
    order_id         uuid not null,
);

create table if not exists booking_products
(
    foreign key (shopping_cart_id) references bookings (shopping_cart_id) on delete cascade,
    primary key (shopping_cart_id, product_id),
    product_id       uuid not null,
    quantity         long not null,
)
