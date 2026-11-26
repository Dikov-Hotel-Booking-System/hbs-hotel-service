package ru.mephi.hbs.hotel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.hbs.hotel.model.api.request.GetHotelsApiRequest;
import ru.mephi.hbs.hotel.model.api.response.GetHotelsApiResponse;
import ru.mephi.hbs.hotel.service.GetHotelsService;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class HotelController {

    public static final String BASE = "/api";
    public static final String GET_HOTELS = BASE + "/hotels";

    GetHotelsService getHotelsService;

    @GetMapping(GET_HOTELS)
    @Operation(
            summary = "получение страницы отелей",
            description = "Для доступа нужен JWT с ролью USER, издателем hbs-booking-service и аудиторией hbs-api.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "страница отелей",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GetHotelsApiResponse.class))),
                    @ApiResponse(responseCode = "400", description = "недопустимые параметры пагинации",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(type = "object"))),
                    @ApiResponse(responseCode = "401", description = "JWT отсутствует или не прошёл проверку",
                            content = @Content),
                    @ApiResponse(responseCode = "403", description = "JWT не содержит роль USER", content = @Content)
            }
    )
    public ResponseEntity<GetHotelsApiResponse> getHotels(@ModelAttribute @Valid GetHotelsApiRequest request) {
        return ResponseEntity.ok(getHotelsService.getHotels(request));
    }

}
