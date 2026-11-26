package ru.mephi.hbs.hotel.config;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.validation.Valid;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.mephi.hbs.hotel.model.api.response.ValidationErrorApiResponse;

import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    OpenAPI hotelOpenApi() {
        Components components = new Components().addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));
        ModelConverters.getInstance(true).readAll(ValidationErrorApiResponse.class)
                .forEach(components::addSchemas);

        return new OpenAPI()
                .info(new Info().title("Сервис отелей").version("v0"))
                .addServersItem(new Server().url("/"))
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

    @Bean
    OperationCustomizer commonErrorResponses() {
        var validationErrorSchema = ModelConverters.getInstance(true).resolveAsResolvedSchema(
                new AnnotatedType(ValidationErrorApiResponse.class).resolveAsRef(true)
        ).schema;

        return (operation, handlerMethod) -> {
            var responses = operation.getResponses();
            if (Arrays.stream(handlerMethod.getMethodParameters()).anyMatch(parameter ->
                    parameter.hasParameterAnnotation(ModelAttribute.class)
                            && parameter.hasParameterAnnotation(Valid.class))) {
                responses.putIfAbsent("400", new ApiResponse()
                        .description("ошибка валидации запроса")
                        .content(new Content().addMediaType(APPLICATION_JSON_VALUE,
                                new MediaType().schema(validationErrorSchema))));
            }
            if (operation.getSecurity() == null) {
                responses.putIfAbsent("401", new ApiResponse().description("JWT отсутствует или не прошёл проверку"));
                responses.putIfAbsent("403", new ApiResponse().description("недостаточно прав для вызова операции"));
            }
            return operation;
        };
    }

}
