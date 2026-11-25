# 🗄️ Database

> `hbs_hotel`. База хранит отели, номера и удержания номеров на даты. Liquibase применяет миграции при запуске сервиса.

## 🧩 Диаграмма базы данных

```mermaid
erDiagram
    hotels ||--o{ rooms : "отель"
    rooms ||--o{ room_reservations : "номер"

    hotels {
        bigint id PK
        varchar name
        varchar address
    }

    rooms {
        bigint id PK
        bigint hotel_id FK
        varchar number
        boolean available
        integer times_booked
    }

    room_reservations {
        bigint booking_id PK
        bigint room_id FK
        date start_date
        date end_date
        boolean released
    }
```

`room_reservations.booking_id` содержит идентификатор брони в базе Booking Service.

---

## 📋 Таблицы

- [hotels](hotels_table.md)
- [rooms](rooms_table.md)
- [room_reservations](room_reservations_table.md)

---

## 🧬 Liquibase

Миграции хранятся в `src/main/resources/liquibase`.

- `changelog-master.yaml` подключает `tables/changelog.yaml`.
- `tables/changelog.yaml` перечисляет миграции в порядке создания таблиц.
- У каждой таблицы свой `changelog.yaml` с парой `init_001.sql` и `rollback_001.sql`.
- `init_001.sql` создаёт таблицу и задаёт комментарии к ней и её полям через `comment on`.
- Для изменений после применения миграции добавляется следующая пара SQL-файлов.
