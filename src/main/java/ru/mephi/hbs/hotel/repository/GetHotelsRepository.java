package ru.mephi.hbs.hotel.repository;

import jakarta.annotation.Nonnull;
import ru.mephi.hbs.hotel.model.sql.request.GetHotelsSqlRequest;
import ru.mephi.hbs.hotel.model.sql.response.GetHotelsSqlResponse;

import java.util.List;

public interface GetHotelsRepository {

    List<GetHotelsSqlResponse> getHotels(@Nonnull GetHotelsSqlRequest request);

    Long countHotels();

}
