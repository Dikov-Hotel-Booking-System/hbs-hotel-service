package ru.mephi.hbs.hotel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.hbs.hotel.dto.response.HotelResponse;
import ru.mephi.hbs.hotel.service.GetHotelsService;

import java.util.List;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@SecurityScheme(name = "bearerAuth", type = HTTP, scheme = "bearer", bearerFormat = "JWT")
public class HotelController {

    public static final String BASE = "/api";
    public static final String HOTELS = BASE + "/hotels";

    GetHotelsService getHotelsService;

    @GetMapping(HOTELS)
    @Operation(summary = "получение списка отелей", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<HotelResponse>> getHotels() {
        return ResponseEntity.ok(getHotelsService.getHotels());
    }

}
