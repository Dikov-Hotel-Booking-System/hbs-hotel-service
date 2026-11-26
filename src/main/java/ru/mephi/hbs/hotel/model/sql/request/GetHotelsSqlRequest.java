package ru.mephi.hbs.hotel.model.sql.request;

import jakarta.annotation.Nonnull;
import lombok.Builder;

@Builder
public record GetHotelsSqlRequest(

        @Nonnull
        Integer limit,

        @Nonnull
        Long offset

) {}
