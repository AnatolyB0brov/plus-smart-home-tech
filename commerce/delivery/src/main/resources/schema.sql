create table if not exists address
(
    address_id uuid default gen_random_uuid() primary key,
    country    varchar(255),
    city       varchar(255),
    street     varchar(255),
    house      varchar(255),
    flat       varchar(255)
);

create table if not exists deliveries
(
    delivery_id    uuid default gen_random_uuid() primary key,
    from_address_id   uuid references address(address_id),
    to_address_id     uuid references address(address_id),
    order_id       uuid,
    delivery_state varchar(50)
);