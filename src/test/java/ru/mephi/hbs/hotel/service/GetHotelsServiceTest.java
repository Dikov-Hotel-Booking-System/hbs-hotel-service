package ru.mephi.hbs.hotel.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.hbs.hotel.mapper.ToApiMapper;
import ru.mephi.hbs.hotel.model.api.request.GetHotelsApiRequest;
import ru.mephi.hbs.hotel.model.api.response.GetHotelsApiResponse;
import ru.mephi.hbs.hotel.model.api.response.GetHotelsApiResponse.HotelItemApiResponse;
import ru.mephi.hbs.hotel.model.sql.request.GetHotelsSqlRequest;
import ru.mephi.hbs.hotel.model.sql.response.GetHotelsSqlResponse;
import ru.mephi.hbs.hotel.repository.GetHotelsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetHotelsServiceTest {

    private static final long FIRST_HOTEL_ID = 1L;
    private static final long SECOND_HOTEL_ID = 2L;
    private static final String NAME = "name";
    private static final String ADDRESS = "address";

    private static final int FIRST_PAGE = 0;
    private static final long FIRST_PAGE_OFFSET = 0L;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final long ONE_PAGE = 1L;
    private static final long TWO_PAGES = 2L;

    private static final GetHotelsSqlResponse FIRST_ROW = buildSqlResponse(FIRST_HOTEL_ID);
    private static final GetHotelsSqlResponse SECOND_ROW = buildSqlResponse(SECOND_HOTEL_ID);
    private static final HotelItemApiResponse FIRST_HOTEL = buildItemApiResponse(FIRST_HOTEL_ID);
    private static final HotelItemApiResponse SECOND_HOTEL = buildItemApiResponse(SECOND_HOTEL_ID);

    @Mock
    GetHotelsRepository getHotelsRepository;

    @Mock
    ToApiMapper<GetHotelsSqlResponse, HotelItemApiResponse> hotelItemApiMapper;

    @Captor
    ArgumentCaptor<GetHotelsSqlRequest> sqlRequestCaptor;

    @InjectMocks
    GetHotelsService getHotelsService;

    @Nested
    class GetHotelsTest {

        @Test
        @DisplayName("если отели найдены, метод возвращает страницу отелей и общее число страниц")
        void hotelsFound_shouldReturnHotelsPage() {
            final int pageSize = 2;
            final long totalElements = 3L;

            when(getHotelsRepository.getHotels(any())).thenReturn(List.of(FIRST_ROW, SECOND_ROW));
            when(getHotelsRepository.countHotels()).thenReturn(totalElements);
            when(hotelItemApiMapper.toApi(FIRST_ROW)).thenReturn(FIRST_HOTEL);
            when(hotelItemApiMapper.toApi(SECOND_ROW)).thenReturn(SECOND_HOTEL);

            GetHotelsApiResponse response = getHotelsService.getHotels(buildRequest(FIRST_PAGE, pageSize));

            assertEquals(FIRST_PAGE, response.page());
            assertEquals(pageSize, response.size());
            assertEquals(totalElements, response.totalElements());
            assertEquals(TWO_PAGES, response.totalPages());
            assertEquals(List.of(FIRST_HOTEL, SECOND_HOTEL), response.hotels());

            verify(getHotelsRepository).getHotels(sqlRequestCaptor.capture());
            assertEquals(pageSize, sqlRequestCaptor.getValue().limit());
            assertEquals(FIRST_PAGE_OFFSET, sqlRequestCaptor.getValue().offset());
            verify(getHotelsRepository).countHotels();
            verify(hotelItemApiMapper).toApi(FIRST_ROW);
            verify(hotelItemApiMapper).toApi(SECOND_ROW);
        }

        @Test
        @DisplayName("если параметры не заданы, метод использует первую страницу размером 20")
        void paginationParamsMissing_shouldUseDefaults() {
            when(getHotelsRepository.getHotels(any())).thenReturn(List.of(FIRST_ROW));
            when(getHotelsRepository.countHotels()).thenReturn(1L);
            when(hotelItemApiMapper.toApi(FIRST_ROW)).thenReturn(FIRST_HOTEL);

            GetHotelsApiResponse response = getHotelsService.getHotels(new GetHotelsApiRequest(null, null));

            assertEquals(FIRST_PAGE, response.page());
            assertEquals(DEFAULT_PAGE_SIZE, response.size());
            assertEquals(ONE_PAGE, response.totalPages());
            assertEquals(List.of(FIRST_HOTEL), response.hotels());

            verify(getHotelsRepository).getHotels(sqlRequestCaptor.capture());
            assertEquals(DEFAULT_PAGE_SIZE, sqlRequestCaptor.getValue().limit());
            assertEquals(FIRST_PAGE_OFFSET, sqlRequestCaptor.getValue().offset());
        }

        @Test
        @DisplayName("если запрошена следующая страница, метод вычисляет смещение")
        void nextPage_shouldCalculateOffset() {
            final int page = 1;
            final int pageSize = 1;

            when(getHotelsRepository.getHotels(any())).thenReturn(List.of(SECOND_ROW));
            when(getHotelsRepository.countHotels()).thenReturn(2L);
            when(hotelItemApiMapper.toApi(SECOND_ROW)).thenReturn(SECOND_HOTEL);

            GetHotelsApiResponse response = getHotelsService.getHotels(buildRequest(page, pageSize));

            assertEquals(page, response.page());
            assertEquals(TWO_PAGES, response.totalPages());
            assertEquals(List.of(SECOND_HOTEL), response.hotels());

            verify(getHotelsRepository).getHotels(sqlRequestCaptor.capture());
            assertEquals(pageSize, sqlRequestCaptor.getValue().limit());
            assertEquals(1L, sqlRequestCaptor.getValue().offset());
        }

        @Test
        @DisplayName("если запрошена далёкая страница, метод сохраняет общее число отелей и вычисляет смещение без переполнения")
        void farPage_shouldCalculateOffsetWithoutOverflow() {
            final int pageSize = 100;
            final long totalElements = 35L;

            when(getHotelsRepository.getHotels(any())).thenReturn(List.of());
            when(getHotelsRepository.countHotels()).thenReturn(totalElements);

            GetHotelsApiResponse response = getHotelsService.getHotels(buildRequest(Integer.MAX_VALUE, pageSize));

            assertEquals(Integer.MAX_VALUE, response.page());
            assertEquals(totalElements, response.totalElements());
            assertEquals(ONE_PAGE, response.totalPages());
            assertEquals(List.of(), response.hotels());

            verify(getHotelsRepository).getHotels(sqlRequestCaptor.capture());
            assertEquals(pageSize, sqlRequestCaptor.getValue().limit());
            assertEquals(214_748_364_700L, sqlRequestCaptor.getValue().offset());
            verifyNoInteractions(hotelItemApiMapper);
        }

        @Test
        @DisplayName("если отелей нет, метод возвращает пустую страницу с нулевым числом страниц")
        void hotelsNotFound_shouldReturnEmptyPage() {
            final int pageSize = 10;
            final long emptyCount = 0L;

            when(getHotelsRepository.getHotels(any())).thenReturn(List.of());
            when(getHotelsRepository.countHotels()).thenReturn(emptyCount);

            GetHotelsApiResponse response = getHotelsService.getHotels(buildRequest(FIRST_PAGE, pageSize));

            assertEquals(FIRST_PAGE, response.page());
            assertEquals(pageSize, response.size());
            assertEquals(emptyCount, response.totalElements());
            assertEquals(emptyCount, response.totalPages());
            assertEquals(List.of(), response.hotels());
            verifyNoInteractions(hotelItemApiMapper);
        }

    }

    private static GetHotelsApiRequest buildRequest(int page, int size) {
        return GetHotelsApiRequest.builder()
                .page(page)
                .size(size)
                .build();
    }

    private static GetHotelsSqlResponse buildSqlResponse(long id) {
        return GetHotelsSqlResponse.builder()
                .id(id)
                .name(NAME)
                .address(ADDRESS)
                .build();
    }

    private static HotelItemApiResponse buildItemApiResponse(long id) {
        return HotelItemApiResponse.builder()
                .id(id)
                .name(NAME)
                .address(ADDRESS)
                .build();
    }

}
