package ru.mephi.hbs.hotel.entity.response;

import jakarta.annotation.Nonnull;
import lombok.Builder;

@Builder
public record GetHotelsEntityResponse(

        @Nonnull
        Long id,
        @Nonnull
        String name,
        @Nonnull
        String address

) {}
