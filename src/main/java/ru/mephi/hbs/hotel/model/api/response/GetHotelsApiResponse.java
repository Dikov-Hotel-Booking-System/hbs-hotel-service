package ru.mephi.hbs.hotel.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record GetHotelsApiResponse(

        @JsonProperty(value = "page", required = true)
        Integer page,

        @JsonProperty(value = "size", required = true)
        Integer size,

        @JsonProperty(value = "total_elements", required = true)
        Long totalElements,

        @JsonProperty(value = "total_pages", required = true)
        Long totalPages,

        @JsonProperty(value = "hotels", required = true)
        List<HotelItemApiResponse> hotels

) {

    @Builder
    public record HotelItemApiResponse(

            @JsonProperty(value = "id", required = true)
            Long id,

            @JsonProperty(value = "name", required = true)
            String name,

            @JsonProperty(value = "address", required = true)
            String address

    ) {}

}
