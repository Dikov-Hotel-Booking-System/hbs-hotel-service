package ru.mephi.hbs.hotel.enums;

import jakarta.annotation.Nonnull;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public enum QueryPath {

    GET_HOTELS("queries/get_hotels/get_hotels.sql");

    @Nonnull
    String sql;

    QueryPath(@Nonnull String path) {
        this.sql = readSql(path);
    }

    public String sql() {
        return sql;
    }

    @Nonnull
    private static String readSql(@Nonnull String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try {
            return resource.getContentAsString(UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("не удалось прочитать файл SQL " + path, exception);
        }
    }

}
