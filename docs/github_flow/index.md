# 🌿 GitHub Flow

> `feature/<название_задачи>` → Pull Request → `master`

---

## 🌳 Ветки

| Ветка | Назначение |
|-------|------------|
| `master` | Основная ветка. Изменения попадают в неё через PR из `feature/*` этого репозитория. |
| `feature/*` | Ветки задач. Каждая начинается от актуальной `master`. |

---

## 🛠️ Работа над задачей

1. Создать ветку от актуальной `master`.

   ```shell
   git switch master
   git pull --ff-only origin master
   git switch -c feature/<название_задачи>
   ```

2. Внести изменения, проверить их и отправить коммит в свою ветку.

   ```shell
   .\gradlew.bat test bootJar
   git add <изменённые_файлы>
   git commit -m "<описание_изменения>"
   git push -u origin feature/<название_задачи>
   ```

3. Открыть PR в `master`. После успешного CI можно слить свой PR.

---

## ⚙️ Проверки CI

| Проверка | Что проверяется |
|----------|-----------------|
| `Branch policy` | PR открыт из `feature/*` этого репозитория. |
| `Unit tests and build` | Юнит-тесты без БД и сборка JAR. |
| `Build Docker image` | Сборка образа из этого JAR. |

Проверки обязательны для слияния. GitHub блокирует прямой и принудительный push
в `master`, а также удаление ветки. CI собирает образ без публикации и деплоя.

[Workflow CI](../../.github/workflows/ci.yml) ·
[Проверка PR](../../.github/workflows/branch-policy.yml) ·
[Правило защиты master](../../.github/rulesets/master.json)
