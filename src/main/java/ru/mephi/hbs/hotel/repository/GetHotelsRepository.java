package ru.mephi.hbs.hotel.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.mephi.hbs.hotel.entity.response.GetHotelsEntityResponse;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static ru.mephi.hbs.hotel.enums.QueryPath.GET_HOTELS;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GetHotelsRepository {

    JdbcClient jdbcClient;

    public List<GetHotelsEntityResponse> getHotels() {
        return jdbcClient.sql(GET_HOTELS.sql())
                .query((rs, rowNum) -> GetHotelsEntityResponse.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .build()
                )
                .list();
    }

}
