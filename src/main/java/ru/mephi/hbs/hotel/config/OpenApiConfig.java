package ru.mephi.hbs.hotel.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String ALL = "/**";

    static final String V3_API_DOCS = "/v3/api-docs";
    static final String V3_API_DOCS_ALL = V3_API_DOCS + ALL;
    static final String V3_API_DOCS_YAML = V3_API_DOCS + ".yaml";

    private static final String SWAGGER_UI = "/swagger-ui";
    static final String SWAGGER_UI_HTML = SWAGGER_UI + ".html";
    static final String SWAGGER_UI_ALL = SWAGGER_UI + ALL;

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    OpenAPI hotelOpenApi() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info().title("Сервис отелей").version("v0"))
                .addServersItem(new Server().url("/"))
                .components(new Components().addSecuritySchemes(BEARER_AUTH, securityScheme))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

}
