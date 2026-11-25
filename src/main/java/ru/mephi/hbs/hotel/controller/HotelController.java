package ru.mephi.hbs.hotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.hbs.hotel.dto.response.HotelResponse;
import ru.mephi.hbs.hotel.service.GetHotelsService;

import java.util.List;

@RestController
public class HotelController {

    public static final String HOTELS = "/api/hotels";

    private final GetHotelsService getHotelsService;

    public HotelController(GetHotelsService getHotelsService) {
        this.getHotelsService = getHotelsService;
    }

    @GetMapping(HOTELS)
    public ResponseEntity<List<HotelResponse>> getHotels() {
        return ResponseEntity.ok(getHotelsService.getHotels());
    }
}
