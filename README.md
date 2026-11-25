# 🚀 Hbs-hotel-service

> Каркас сервиса отелей и номеров гостиничной системы

---

## 📌 Описание

Сервис предназначен для работы с отелями и номерами. На текущем этапе созданы
стартовый класс Spring Boot, базовая конфигурация, миграции БД, сборка JAR и Docker-образа, а также CI.

---

## 📘 Документация по процессам

- ### [GitHub Flow](docs/github_flow/index.md)
- ### [Database](docs/database/index.md)

---

## 🛠️ Стек технологий

- ☕ Java 21 (язык разработки)
- 🌱 Spring Boot 4.1.2 и Spring Web MVC (основа приложения)
- 🐘 PostgreSQL, Spring Data JPA и Liquibase (работа с базой данных и миграции)
- 🔐 Spring Security OAuth2 Resource Server (зависимости для работы с JWT)
- ✅ Bean Validation (валидация данных)
- 🔎 Spring Cloud 2025.1.4 и Eureka Discovery Client (регистрация сервисов)
- 🐘 Gradle 9.5.1 с Kotlin DSL (сборка)
- 🧪 JUnit Jupiter и Mockito (юнит-тесты)
- 🐳 Docker и GitHub Actions (сборка образа и CI)

---

## 🚀 Локальный запуск

1. Запустить PostgreSQL, создать отдельную базу сервиса и узнать параметры подключения

   | Параметр | Где взять |
   |----------|-----------|
   | `<db_url>` | JDBC URL PostgreSQL, например `jdbc:postgresql://localhost:5432/hbs_hotel` |
   | `<db_username>` | Имя пользователя PostgreSQL |
   | `<db_password>` | Пароль пользователя PostgreSQL |

   Для регистрации приложения сначала запустить [hbs-eureka-server](https://github.com/Dikov-Hotel-Booking-System/hbs-eureka-server).

2. Создать папку `config` в корне проекта и файл `config/application.yaml` с содержимым ниже
   ```yaml
   spring:
     datasource:
       url: "<db_url>"
       username: "<db_username>"
       password: "<db_password>"

   eureka:
     client:
       service-url:
         defaultZone: "http://localhost:8761/eureka/"
   ```

   Локальный файл переопределяет значения из `src/main/resources/application.yaml`
   через `spring.config.import: optional:file:./config/application.yaml`.
   Папка `config` исключена из Git.

   В локальном файле указываются реальные параметры своей базы данных.

3. Из корня проекта выполнить команду
   ```shell
   .\gradlew.bat bootRun
   ```

   Liquibase применяет миграции к базе при запуске сервиса.

   При запуске Spring Boot выбирает свободный порт. Его видно в журнале запуска и в Eureka.

---

## 📦 Сборка JAR и Docker-образа

```shell
.\gradlew.bat bootJar
docker build --tag hbs-hotel-service:local .
```

Исполняемый JAR находится в `build/libs/app.jar`. Dockerfile использует JRE 21
и запускает приложение от пользователя `10001:10001`.

---

## 🔗 Полезные ссылки

| Ссылка | Примечание |
|--------|------------|
| [Репозиторий](https://github.com/Dikov-Hotel-Booking-System/hbs-hotel-service) | Исходный код сервиса |
| [GitHub Actions](https://github.com/Dikov-Hotel-Booking-System/hbs-hotel-service/actions) | Запуски CI |

---

## 🧪 Тестирование сервиса

Юнит-тесты запускаются командой
```shell
.\gradlew.bat test
```

Для них используются JUnit Jupiter и Mockito. Подключение к PostgreSQL
и запуск Spring-контекста не требуются. Пока тестовых классов нет,
задача `test` завершается с `NO-SOURCE`.

---
