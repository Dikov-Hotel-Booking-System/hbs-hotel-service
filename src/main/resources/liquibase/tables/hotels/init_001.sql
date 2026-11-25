create table hotels (
    id      bigint generated always as identity,
    name    varchar(255) not null,
    address varchar(255) not null,

    constraint pk_hotels primary key (id)
);

comment on table hotels is 'отели гостиничной системы';
comment on column hotels.id is 'идентификатор отеля';
comment on column hotels.name is 'название отеля';
comment on column hotels.address is 'адрес отеля';
