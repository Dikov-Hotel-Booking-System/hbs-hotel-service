package ru.mephi.hbs.hotel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public enum ErrorType {

    VALIDATION_ERROR(9001, BAD_REQUEST, "ошибка валидации запроса"),
    INTERNAL_ERROR(9999, INTERNAL_SERVER_ERROR, "внутренняя ошибка сервера"),
    ;

    int code;
    HttpStatus status;
    String message;

}
