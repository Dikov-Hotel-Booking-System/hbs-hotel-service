package ru.mephi.hbs.hotel.model.api.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetHotelsApiRequestTest {

    private static final int FIRST_PAGE = 0;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String PAGE = "page";
    private static final String SIZE = "size";

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    @AfterAll
    static void closeValidatorFactory() {
        VALIDATOR_FACTORY.close();
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("invalidRequests")
    @DisplayName("Невалидные запросы дают одну ошибку на ожидаемом поле")
    void invalidRequest(
            GetHotelsApiRequest request,
            String testName,
            String expectedField,
            String expectedMessage
    ) {
        Set<ConstraintViolation<GetHotelsApiRequest>> violations = VALIDATOR.validate(request);

        assertEquals(1, violations.size());

        ConstraintViolation<GetHotelsApiRequest> violation = violations.iterator().next();
        assertEquals(expectedField, violation.getPropertyPath().toString());
        assertEquals(expectedMessage, violation.getMessage());
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("validRequests")
    @DisplayName("Валидные запросы проходят проверку")
    void validRequest(GetHotelsApiRequest request, String testName) {
        Set<ConstraintViolation<GetHotelsApiRequest>> violations = VALIDATOR.validate(request);

        assertTrue(violations.isEmpty());
    }

    private static @NonNull Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of(buildRequest(-1, MIN_PAGE_SIZE), "отрицательный номер страницы",
                        PAGE, "номер страницы не может быть меньше 0"),
                Arguments.of(buildRequest(FIRST_PAGE, 0), "нулевой размер страницы",
                        SIZE, "размер страницы не может быть меньше 1"),
                Arguments.of(buildRequest(FIRST_PAGE, MAX_PAGE_SIZE + 1), "размер страницы больше допустимого",
                        SIZE, "размер страницы не может быть больше 100")
        );
    }

    private static @NonNull Stream<Arguments> validRequests() {
        return Stream.of(
                Arguments.of(new GetHotelsApiRequest(null, null), "параметры не заданы"),
                Arguments.of(buildRequest(FIRST_PAGE, MIN_PAGE_SIZE), "минимальные номер и размер страницы"),
                Arguments.of(buildRequest(FIRST_PAGE, MAX_PAGE_SIZE), "максимальный размер страницы")
        );
    }

    private static GetHotelsApiRequest buildRequest(int page, int size) {
        return GetHotelsApiRequest.builder()
                .page(page)
                .size(size)
                .build();
    }

}
