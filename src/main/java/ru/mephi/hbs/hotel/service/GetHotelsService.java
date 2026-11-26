package ru.mephi.hbs.hotel.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.hbs.hotel.mapper.ToApiMapper;
import ru.mephi.hbs.hotel.model.api.request.GetHotelsApiRequest;
import ru.mephi.hbs.hotel.model.api.response.GetHotelsApiResponse;
import ru.mephi.hbs.hotel.model.api.response.GetHotelsApiResponse.HotelItemApiResponse;
import ru.mephi.hbs.hotel.model.sql.request.GetHotelsSqlRequest;
import ru.mephi.hbs.hotel.model.sql.response.GetHotelsSqlResponse;
import ru.mephi.hbs.hotel.repository.GetHotelsRepository;

import java.util.List;

import static java.util.Objects.requireNonNullElse;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static ru.mephi.hbs.hotel.model.api.request.GetHotelsApiRequest.DEFAULT_PAGE;
import static ru.mephi.hbs.hotel.model.api.request.GetHotelsApiRequest.DEFAULT_SIZE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GetHotelsService {

    GetHotelsRepository getHotelsRepository;

    ToApiMapper<GetHotelsSqlResponse, HotelItemApiResponse> hotelItemApiMapper;

    @Transactional(readOnly = true, isolation = REPEATABLE_READ)
    public GetHotelsApiResponse getHotels(@Nonnull GetHotelsApiRequest params) {
        int page = requireNonNullElse(params.page(), DEFAULT_PAGE);
        int size = requireNonNullElse(params.size(), DEFAULT_SIZE);

        List<HotelItemApiResponse> hotels = getHotelsRepository.getHotels(GetHotelsSqlRequest.builder()
                        .limit(size)
                        .offset((long) page * size)
                        .build()
                )
                .stream()
                .map(hotelItemApiMapper::toApi)
                .toList();

        long totalElements = getHotelsRepository.countHotels();

        return GetHotelsApiResponse.builder()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(Math.ceilDiv(totalElements, size))
                .hotels(hotels)
                .build();
    }

}
