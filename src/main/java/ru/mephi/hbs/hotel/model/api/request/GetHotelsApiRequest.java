package ru.mephi.hbs.hotel.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import org.springdoc.core.annotations.ParameterObject;

@Builder
@ParameterObject
public record GetHotelsApiRequest(

        @Schema(defaultValue = "" + GetHotelsApiRequest.DEFAULT_PAGE)
        @Min(value = 0, message = "номер страницы не может быть меньше {value}")
        Integer page,

        @Schema(defaultValue = "" + GetHotelsApiRequest.DEFAULT_SIZE)
        @Min(value = 1, message = "размер страницы не может быть меньше {value}")
        @Max(value = 100, message = "размер страницы не может быть больше {value}")
        Integer size

) {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;

}
