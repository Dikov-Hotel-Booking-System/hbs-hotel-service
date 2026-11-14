# hbs-hotel-service

Сервис отелей, номеров и резервирования интервалов Hotel Booking System.
На этом этапе создан каркас приложения без бизнес-логики.

Проект сгенерирован через [Spring Initializr](https://start.spring.io): Java 21,
Spring Boot 4.1.2, Spring Cloud 2025.1.4, Gradle Kotlin DSL. Group: `ru.mephi`,
пакет: `ru.mephi.hbs.hotel`.

Зависимости: Spring Web MVC, Spring Data JPA, PostgreSQL Driver, Bean Validation,
OAuth2 Resource Server (Spring Security и JWT), Eureka Discovery Client.
Для юнит-тестов подключены JUnit Jupiter и Mockito.

Юнит-тесты и сборка:

```powershell
.\gradlew.bat test bootJar
```

Для запуска приложения нужна база PostgreSQL. Укажите
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
в окружении, затем выполните `.\gradlew.bat bootRun`.
Пароли в репозиторий не добавляйте.

Задача Gradle `test` предназначена для юнит-тестов без Spring-контекста
и подключения к БД. Тесты размещаются в `src/test/java`.
Пока тестовых классов нет, задача завершается с `NO-SOURCE`.

Порт приложения: `8082`. Eureka по умолчанию ожидается по адресу
`http://localhost:8761/eureka/`.

## Docker

Исполняемый JAR всегда называется `build/libs/app.jar`; обычный библиотечный
JAR не собирается. Сначала соберите JAR, затем образ:

```powershell
.\gradlew.bat bootJar
docker build --tag hbs-hotel-service:local .
```

Dockerfile использует JRE 21 и запускает приложение от UID/GID `10001:10001`.
В образ копируется уже собранный JAR.

## GitHub Actions

Workflow [`.github/workflows/ci.yml`](.github/workflows/ci.yml) запускается
при push в `master`, `develop`, `feature/**`, для PR в `master`/`develop`
и вручную через `workflow_dispatch`. Содержимое workflow одинаково
во всех четырёх репозиториях.

Рабочий поток: `feature/<name>` → PR → `develop` → PR → `master`.
Проверка `Branch policy` отклоняет PR из других исходных веток.
Правила защиты для GitHub и порядок первоначальной настройки описаны
в [`.github/BRANCHING.md`](.github/BRANCHING.md).

1. `build` выполняет `test bootJar`: запускает юнит-тесты и собирает JAR.
2. `docker` получает тот же JAR через artifact и собирает образ с тегом SHA коммита.

JAR и отчёты юнит-тестов сохраняются в artifacts на 7 дней. Образ проверяется сборкой,
без публикации в registry. Деплоя и Kubernetes-манифестов нет.

Для юнит-тестов внешние сервисы и секреты не требуются.
