package ru.mephi.hbs.hotel.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ValidationErrorApiResponse(

        @JsonProperty(value = "error_code", required = true)
        Integer errorCode,
        @JsonProperty(value = "message", required = true)
        String message,
        @JsonProperty(value = "field_errors", required = true)
        List<FieldErrorItemApiResponse> fieldErrors

) {

    @Builder
    public record FieldErrorItemApiResponse(
            @JsonProperty(value = "field", required = true)
            String field,
            @JsonProperty(value = "message", required = true)
            String message
    ) {}

}
