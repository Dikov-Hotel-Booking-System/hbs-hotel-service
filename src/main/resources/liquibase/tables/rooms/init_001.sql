create table rooms (
    id           bigint generated always as identity,
    hotel_id     bigint      not null,
    number       varchar(50) not null,
    available    boolean     not null default true,
    times_booked integer     not null default 0,

    constraint pk_rooms primary key (id),
    constraint fk_rooms_hotels foreign key (hotel_id) references hotels (id),
    constraint uq_rooms_hotel_number unique (hotel_id, number),
    constraint chk_rooms_times_booked check (times_booked >= 0)
);

comment on table rooms is 'номера отелей с доступностью и счётчиками бронирований';
comment on column rooms.id is 'идентификатор номера';
comment on column rooms.hotel_id is 'идентификатор отеля, которому принадлежит номер';
comment on column rooms.number is 'обозначение номера в пределах отеля';
comment on column rooms.available is 'операционная доступность номера для бронирования';
comment on column rooms.times_booked is 'счётчик бронирований номера для подбора';
