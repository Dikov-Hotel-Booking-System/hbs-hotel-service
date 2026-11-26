package ru.mephi.hbs.hotel.exception;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mephi.hbs.hotel.model.api.response.ErrorApiResponse;
import ru.mephi.hbs.hotel.model.api.response.ValidationErrorApiResponse;
import ru.mephi.hbs.hotel.model.api.response.ValidationErrorApiResponse.FieldErrorItemApiResponse;

import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static ru.mephi.hbs.hotel.exception.ErrorType.INTERNAL_ERROR;
import static ru.mephi.hbs.hotel.exception.ErrorType.VALIDATION_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ValidationErrorApiResponse handleMethodArgumentNotValidException(
            @Nonnull MethodArgumentNotValidException exception
    ) {
        List<FieldErrorItemApiResponse> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(GlobalExceptionHandler::mapToFieldErrorApiResponse)
                .toList();

        return ValidationErrorApiResponse.builder()
                .errorCode(VALIDATION_ERROR.getCode())
                .message(VALIDATION_ERROR.getMessage())
                .fieldErrors(fieldErrors)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorApiResponse handleException(@Nonnull Exception exception) throws Exception {
        if (exception instanceof ErrorResponse) {
            throw exception;
        }

        log.error("Неожиданная ошибка", exception);

        return ErrorApiResponse.builder()
                .errorCode(INTERNAL_ERROR.getCode())
                .message(INTERNAL_ERROR.getMessage())
                .build();
    }

    private static FieldErrorItemApiResponse mapToFieldErrorApiResponse(@Nonnull FieldError fieldError) {
        return FieldErrorItemApiResponse.builder()
                .field(fieldError.getField().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT))
                .message(fieldError.isBindingFailure() ? "недопустимое значение" : fieldError.getDefaultMessage())
                .build();
    }

}
