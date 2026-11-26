# 🌐 Получение списка отелей

> Возвращает страницу отелей в порядке возрастания идентификатора.

## ⚙️ Основные характеристики

| Характеристика | Значение |
|----------------|----------|
| URL | `/api/hotels` |
| URL через API Gateway | `http://localhost:8080/api/hotels` |
| Метод | `GET` |
| Доступ | JWT с ролью `USER` |
| Код успешного ответа | `200` |

## 📥 Параметры запроса

| Параметр | Тип | Обязательность поля | По умолчанию | Допустимые значения |
|----------|-----|---------------------|--------------|---------------------|
| `page` | `number` | ❌ | `0` | Целое число от `0` |
| `size` | `number` | ❌ | `20` | Целое число от `1` до `100` |

## 📤 Успешный ответ

| Поле JSON | Тип | Обязательность поля | Описание |
|-----------|-----|---------------------|----------|
| `page` | `number` | ✅ | Номер запрошенной страницы |
| `size` | `number` | ✅ | Размер страницы |
| `total_elements` | `number` | ✅ | Общее число отелей |
| `total_pages` | `number` | ✅ | Общее число страниц |
| `hotels` | `array` | ✅ | Отели на запрошенной странице |

При пустой базе `total_pages` равно `0`. Если на запрошенной странице нет отелей, `hotels` содержит пустой массив.

Поля элемента массива `hotels` описаны ниже.

| Поле JSON | Тип | Обязательность поля | Описание |
|-----------|-----|---------------------|----------|
| `id` | `number` | ✅ | Идентификатор отеля |
| `name` | `string` | ✅ | Название отеля |
| `address` | `string` | ✅ | Адрес отеля |

```json
{
  "page": 0,
  "size": 20,
  "total_elements": 1,
  "total_pages": 1,
  "hotels": [
    {
      "id": 1,
      "name": "<название_отеля>",
      "address": "<адрес_отеля>"
    }
  ]
}
```

## ⚠️ Ошибки

| HTTP-статус | `error_code` | Описание |
|-------------|--------------|----------|
| `400` | `9001` | Некорректные параметры пагинации. `field_errors` содержит список полей и сообщений. |
| `500` | `9999` | Внутренняя ошибка сервиса. Ответ содержит `error_code` и `message`. |

Ответ при запросе с `page=-1`

```json
{
  "error_code": 9001,
  "message": "ошибка валидации запроса",
  "field_errors": [
    {
      "field": "page",
      "message": "номер страницы не может быть меньше 0"
    }
  ]
}
```

## 🔒 Проверка доступа

Запрос передаёт заголовок `Authorization: Bearer <токен>`. Сервис проверяет подпись по открытому ключу, срок действия, `iss=hbs-booking-service` и `aud=hbs-api`. Для запроса требуется `role=USER`.

- `401` — токен отсутствует или не прошёл проверку.
- `403` — действительный токен без роли `USER`.

Путь к открытому ключу RSA в формате PEM задаётся в локальном `config/application.yaml`.

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          public-key-location: "file:<путь_к_открытому_ключу>"
```

## 🔁 Последовательность вызовов

```mermaid
sequenceDiagram
    actor User as Пользователь
    participant Gateway as API Gateway
    participant API as HotelController
    participant Service as GetHotelsService
    participant Repository as GetHotelsRepository
    participant DB as PostgreSQL
    participant Mapper as ToApiMapper
    User ->> Gateway: GET /api/hotels?page=0&size=20
    Gateway ->> API: GET /api/hotels?page=0&size=20
    API ->> Service: getHotels(GetHotelsApiRequest)
    Service ->> Repository: getHotels(GetHotelsSqlRequest)
    Repository ->> DB: Запрос страницы отелей
    DB -->> Repository: Строки таблицы hotels
    Repository -->> Service: Список GetHotelsSqlResponse
    loop Для каждой записи
        Service ->> Mapper: toApi(row)
        Mapper -->> Service: HotelItemApiResponse
    end
    Service ->> Repository: countHotels()
    Repository ->> DB: Подсчёт отелей
    DB -->> Repository: Общее число отелей
    Repository -->> Service: Число отелей
    Service -->> API: GetHotelsApiResponse
    API -->> Gateway: 200 OK
    Gateway -->> User: 200 OK
```

## 🧠 Алгоритм

1. Сервис определяет `page` и `size`, затем вычисляет смещение `page * size`.
2. Репозиторий читает страницу в порядке `id`. [Запрос страницы](../../src/main/resources/queries/get_hotels/get_hotels.sql)
3. Маппер преобразует каждую строку в `HotelItemApiResponse`.
4. Репозиторий считает общее число отелей в той же транзакции. Сервис вычисляет общее число страниц. [Подсчёт отелей](../../src/main/resources/queries/get_hotels/count_hotels.sql)
5. Контроллер возвращает страницу отелей со статусом `200`.
