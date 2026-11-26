package ru.mephi.hbs.hotel.mapper.impl;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;
import ru.mephi.hbs.hotel.mapper.ToApiMapper;
import ru.mephi.hbs.hotel.model.api.response.GetHotelsApiResponse.HotelItemApiResponse;
import ru.mephi.hbs.hotel.model.sql.response.GetHotelsSqlResponse;

@Component
public class HotelItemApiMapper implements ToApiMapper<GetHotelsSqlResponse, HotelItemApiResponse> {

    @Override
    public HotelItemApiResponse toApi(@Nonnull GetHotelsSqlResponse source) {
        return HotelItemApiResponse.builder()
                .id(source.id())
                .name(source.name())
                .address(source.address())
                .build();
    }

}
