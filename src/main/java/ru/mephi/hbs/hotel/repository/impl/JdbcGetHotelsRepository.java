package ru.mephi.hbs.hotel.repository.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.mephi.hbs.hotel.model.sql.request.GetHotelsSqlRequest;
import ru.mephi.hbs.hotel.model.sql.response.GetHotelsSqlResponse;
import ru.mephi.hbs.hotel.repository.GetHotelsRepository;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static ru.mephi.hbs.hotel.enums.QueryPath.COUNT_HOTELS;
import static ru.mephi.hbs.hotel.enums.QueryPath.GET_HOTELS;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JdbcGetHotelsRepository implements GetHotelsRepository {

    JdbcClient jdbcClient;

    @Override
    public List<GetHotelsSqlResponse> getHotels(@Nonnull GetHotelsSqlRequest request) {
        return jdbcClient.sql(GET_HOTELS.sql())
                .param("limit", request.limit())
                .param("offset", request.offset())
                .query((rs, rowNum) -> GetHotelsSqlResponse.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .build()
                )
                .list();
    }

    @Override
    public Long countHotels() {
        return jdbcClient.sql(COUNT_HOTELS.sql())
                .query(Long.class)
                .single();
    }

}
