# Правила оформления Git-коммитов

## Общий формат

Все коммиты должны оформляться по шаблону:

```
<type>: <краткое описание изменения>
```

Пример:

```
feat: add game collection screen
fix: fix login validation
docs: update API documentation
```

---

# Типы коммитов

## feat — новая функциональность

Используется при добавлении нового функционала.

Примеры:

```
feat: add user registration
feat: implement game search
feat: add party creation screen
```

---

## fix — исправление ошибки

Используется для исправления багов.

Примеры:

```
fix: fix incorrect game sorting
fix: resolve token refresh error
fix: fix database migration issue
```

---

## docs — документация

Изменения только в документации.

Примеры:

```
docs: add API documentation
docs: update database schema
docs: improve README
```

---

## style — форматирование и стиль

Изменения, которые не меняют логику.

Примеры:

```
style: format Kotlin files
style: fix code indentation
```

---

## refactor — переработка кода

Изменение структуры кода без добавления новой функциональности.

Примеры:

```
refactor: simplify authentication service
refactor: improve repository structure
```

---

## test — тесты

Добавление или изменение тестов.

Примеры:

```
test: add user service tests
test: add game repository tests
```

---

## chore — служебные изменения

Настройки проекта, зависимости, конфигурация.

Примеры:

```
chore: update Gradle dependencies
chore: configure Docker environment
chore: add CI pipeline
```

---

# Правила написания сообщений

## 1. Используем английский язык

Правильно:

```
feat: add profile screen
```

Неправильно:

```
добавил экран профиля
```

---

## 2. Используем повелительную форму

Правильно:

```
add game search
fix login error
update dependencies
```

Неправильно:

```
added game search
fixed login error
```

---

## 3. Один коммит — одно логическое изменение

Плохо:

```
feat: add everything
```

Внутри:

- экран игр
    
- авторизация
    
- база данных
    
- настройки
    

Хорошо:

```
feat: add authentication screen
feat: add local database
feat: add game collection screen
```

---

# Примеры

## Android

```
feat: add Material3 theme
feat: implement game card component
feat: add collection screen
fix: fix navigation between screens
refactor: move network layer to core module
```

---

## Backend

```
feat: add user authentication API
feat: implement games endpoint
fix: fix JWT validation
refactor: separate game service layer
```

---

## Database

```
feat: add users table
feat: create games migration
fix: update database constraints
```

---

# Ветки

Формат названия веток:

```
<type>/POLKA-<номер>-<описание>
```

Примеры:

```
feature/POLKA-15-login-screen
feature/POLKA-23-game-search
bugfix/POLKA-31-token-error
docs/POLKA-5-api-documentation
```

---

# Pull Request

Название PR:

```
POLKA-15: Add login screen
```

Перед созданием PR:

- код собирается;
    
- нет ошибок линтера;
    
- изменения протестированы;
    
- описание PR заполнено.
    

---

# Запрещено

Коммиты:

```
fix
update
changes
123
asdf
```

Большие коммиты с разными задачами.

Прямой push в main.
