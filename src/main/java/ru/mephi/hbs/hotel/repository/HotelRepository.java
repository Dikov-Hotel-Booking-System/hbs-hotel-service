package ru.mephi.hbs.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.hbs.hotel.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
