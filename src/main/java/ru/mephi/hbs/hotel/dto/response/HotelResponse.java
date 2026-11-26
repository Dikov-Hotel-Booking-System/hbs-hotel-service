package ru.mephi.hbs.hotel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record HotelResponse(

        @JsonProperty(value = "id", required = true)
        Long id,
        @JsonProperty(value = "name", required = true)
        String name,
        @JsonProperty(value = "address", required = true)
        String address

) {}
