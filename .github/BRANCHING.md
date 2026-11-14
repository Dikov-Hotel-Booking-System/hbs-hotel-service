# Ветки и правила слияния

Рабочий поток: `feature/<name>` → PR → `develop` → PR → `master`.
`feature/abc` — название ветки, создавать папку `feature` не нужно.

- `master` принимает только PR из `develop` этого же репозитория.
- `develop` принимает только PR из веток `feature/*`.
- Прямые push в `master` и `develop` должны блокироваться правилами GitHub.

## Проверки

Файлы workflow одинаковы во всех четырёх репозиториях.

- `ci.yml`: юнит-тесты, сборка JAR, затем сборка Docker-образа из этого JAR.
  Запускается при push в `master`, `develop`, `feature/**`,
  при PR в `master`/`develop` и вручную.
- `branch-policy.yml`: проверка направления PR; повторяется также при смене
  целевой ветки. Использует `pull_request_target` и только метаданные PR:
  checkout и исполнение кода из PR в этом workflow отсутствуют.

В CI используется задача Gradle `test` для юнит-тестов. Пока тестовых классов
нет, она завершается с `NO-SOURCE`. PostgreSQL и Spring-контекст для тестов
не запускаются. Образы не публикуются, деплоя нет.

## Включение защиты на GitHub

JSON в [rulesets/master.json](rulesets/master.json) и
[rulesets/develop.json](rulesets/develop.json) — подготовленные правила
для импорта. Наличие этих файлов в Git само по себе защиту не включает.

Порядок первоначальной настройки:

1. Отправить initial commit в `master` пустого репозитория.
2. Создать `develop` от этого коммита и отправить её на GitHub.
3. Установить `master` веткой по умолчанию и дождаться успешного CI.
4. В Settings → Rules → Rulesets → New ruleset → Import a ruleset
   импортировать оба JSON, проверить параметры и включить правила.
5. Дальнейшие изменения проводить через `feature/*` → `develop` → `master`.

Оба ruleset требуют PR и успешные проверки:

- `Branch policy`;
- `Unit tests and build`;
- `Build Docker image`.

Обязательные approvals не заданы: пользователь может сливать собственный PR
после успешного CI. Запрещены force push и удаление защищённых веток.
Постоянных исключений для прямого push в правилах нет.

Для `develop` требуется актуальная версия целевой ветки перед слиянием.
Для `master` это требование отключено, чтобы каждое продвижение `develop`
не требовало предварительного обратного слияния `master` в `develop`.

Документация GitHub: [импорт ruleset](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/managing-rulesets-for-a-repository#importing-a-ruleset),
[события PR](https://docs.github.com/en/actions/reference/workflows-and-actions/events-that-trigger-workflows#pull_request_target).
