package ru.mephi.hbs.hotel.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.hbs.hotel.dto.response.HotelResponse;
import ru.mephi.hbs.hotel.repository.GetHotelsRepository;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GetHotelsService {

    GetHotelsRepository getHotelsRepository;

    @Transactional(readOnly = true)
    public List<HotelResponse> getHotels() {
        return getHotelsRepository.getHotels()
                .stream()
                .map(entity -> HotelResponse.builder()
                        .id(entity.id())
                        .name(entity.name())
                        .address(entity.address())
                        .build()
                )
                .toList();
    }

}
