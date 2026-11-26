package ru.mephi.hbs.hotel.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ErrorApiResponse(

        @JsonProperty(value = "error_code", required = true)
        Integer errorCode,
        @JsonProperty(value = "message", required = true)
        String message

) {}
