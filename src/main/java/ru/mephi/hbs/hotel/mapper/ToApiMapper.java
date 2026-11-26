package ru.mephi.hbs.hotel.mapper;

import jakarta.annotation.Nonnull;

public interface ToApiMapper<S, T> {

    T toApi(@Nonnull S source);

}
