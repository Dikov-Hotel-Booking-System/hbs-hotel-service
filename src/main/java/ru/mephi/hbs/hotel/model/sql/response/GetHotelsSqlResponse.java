package ru.mephi.hbs.hotel.model.sql.response;

import jakarta.annotation.Nonnull;
import lombok.Builder;

@Builder
public record GetHotelsSqlResponse(

        @Nonnull
        Long id,

        @Nonnull
        String name,

        @Nonnull
        String address

) {}
