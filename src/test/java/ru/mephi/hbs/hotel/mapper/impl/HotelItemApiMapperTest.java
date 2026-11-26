package ru.mephi.hbs.hotel.mapper.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mephi.hbs.hotel.model.api.response.GetHotelsApiResponse.HotelItemApiResponse;
import ru.mephi.hbs.hotel.model.sql.response.GetHotelsSqlResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HotelItemApiMapperTest {

    private static final long ID = 7L;
    private static final String NAME = "name";
    private static final String ADDRESS = "address";

    @Test
    @DisplayName("маппер переносит идентификатор, название и адрес отеля в ответ")
    void sqlResponse_shouldMapAllHotelFields() {
        HotelItemApiMapper mapper = new HotelItemApiMapper();
        GetHotelsSqlResponse source = GetHotelsSqlResponse.builder()
                .id(ID)
                .name(NAME)
                .address(ADDRESS)
                .build();

        HotelItemApiResponse response = mapper.toApi(source);

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(ADDRESS, response.address());
    }

}
