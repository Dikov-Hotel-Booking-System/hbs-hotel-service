package ru.mephi.hbs.hotel.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.hbs.hotel.dto.response.HotelResponse;
import ru.mephi.hbs.hotel.repository.HotelRepository;

import java.util.List;

@Service
public class GetHotelsService {

    private final HotelRepository hotelRepository;

    public GetHotelsService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Transactional(readOnly = true)
    public List<HotelResponse> getHotels() {
        return hotelRepository.findAll(Sort.by("id")).stream()
                .map(hotel -> new HotelResponse(hotel.getId(), hotel.getName(), hotel.getAddress()))
                .toList();
    }
}
