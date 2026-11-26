package ru.mephi.hbs.hotel.exception;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mephi.hbs.hotel.model.api.response.ErrorApiResponse;
import ru.mephi.hbs.hotel.model.api.response.ValidationErrorApiResponse;

import java.util.List;

import static ru.mephi.hbs.hotel.exception.ErrorType.INTERNAL_ERROR;
import static ru.mephi.hbs.hotel.exception.ErrorType.VALIDATION_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorApiResponse> handleMethodArgumentNotValidException(
            @Nonnull MethodArgumentNotValidException exception
    ) {
        List<ValidationErrorApiResponse.FieldErrorItemApiResponse> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(GlobalExceptionHandler::mapToFieldErrorResponse)
                .toList();

        return ResponseEntity.status(VALIDATION_ERROR.getStatus())
                .body(ValidationErrorApiResponse.builder()
                        .errorCode(VALIDATION_ERROR.getCode())
                        .message(VALIDATION_ERROR.getMessage())
                        .fieldErrors(fieldErrors)
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponse> handleException(@Nonnull Exception exception) {
        log.error("Неожиданная ошибка", exception);

        return buildErrorResponse(
                INTERNAL_ERROR,
                INTERNAL_ERROR.getMessage()
        );
    }

    @Nonnull
    private static ValidationErrorApiResponse.FieldErrorItemApiResponse mapToFieldErrorResponse(
            @Nonnull FieldError fieldError
    ) {
        return ValidationErrorApiResponse.FieldErrorItemApiResponse.builder()
                .field(toSnakeCase(fieldError.getField()))
                .message(fieldError.getDefaultMessage())
                .build();
    }

    @Nonnull
    private ResponseEntity<ErrorApiResponse> buildErrorResponse(
            @Nonnull ErrorType errorType,
            String message
    ) {
        return ResponseEntity.status(errorType.getStatus())
                .body(ErrorApiResponse.builder()
                        .errorCode(errorType.getCode())
                        .message(message)
                        .build()
                );
    }

    @Nonnull
    private static String toSnakeCase(@Nonnull String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

}
