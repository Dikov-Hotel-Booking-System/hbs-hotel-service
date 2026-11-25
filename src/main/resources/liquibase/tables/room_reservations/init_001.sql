create table room_reservations (
    booking_id bigint  not null,
    room_id    bigint  not null,
    start_date date    not null,
    end_date   date    not null,
    released   boolean not null default false,

    constraint pk_room_reservations primary key (booking_id),
    constraint fk_room_reservations_rooms foreign key (room_id) references rooms (id),
    constraint chk_room_reservations_dates check (start_date < end_date)
);

comment on table room_reservations is 'удержания номеров на даты по идентификаторам брони';
comment on column room_reservations.booking_id is 'идентификатор брони в hbs-booking-service';
comment on column room_reservations.room_id is 'идентификатор удерживаемого номера';
comment on column room_reservations.start_date is 'дата заезда, включается в период удержания';
comment on column room_reservations.end_date is 'дата выезда, исключается из периода удержания';
comment on column room_reservations.released is 'признак освобождения номера на даты удержания';
