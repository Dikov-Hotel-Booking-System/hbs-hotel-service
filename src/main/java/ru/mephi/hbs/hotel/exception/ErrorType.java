package ru.mephi.hbs.hotel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public enum ErrorType {

    VALIDATION_ERROR(9001, "ошибка валидации запроса"),
    INTERNAL_ERROR(9999, "внутренняя ошибка сервера");

    int code;
    String message;

}
