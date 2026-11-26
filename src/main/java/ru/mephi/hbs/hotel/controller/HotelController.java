package ru.mephi.hbs.hotel.controller;

import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class HotelController {

    public static final String BASE = "/api";
    public static final String GET_HOTELS = BASE + "/hotels";

    GetHotelsService getHotelsService;

    @GetMapping(GET_HOTELS)
    @Operation(summary = "получение страницы отелей")
    public ResponseEntity<GetHotelsApiResponse> getHotels(@ModelAttribute @Valid GetHotelsApiRequest request) {
        return ResponseEntity.ok(getHotelsService.getHotels(request));
    }

}
