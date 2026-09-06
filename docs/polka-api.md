# Polka API

Base URL: `/api`

Все роуты, кроме `/auth/*`, требуют заголовок:

```http
Authorization: Bearer <access_token>
```

## Auth

### `POST /api/auth/register`
```json
{"login":"user","password":"pass","email":"user@mail.com","username":"User"}
```
Ответ: `201 Created`.

### `POST /api/auth/login`
```json
{"login":"user","password":"pass"}
```
Ответ:
```json
{"access_token":"...","token_type":"Bearer","refresh_token":"...","access_expires_at":0,"refresh_expires_at":0}
```

### `POST /api/auth/refresh`
```json
{"refresh_token":"..."}
```
Ответ:
```json
{"access_token":"...","refresh_token":"...","token_type":"Bearer","expires_at":0}
```

### `POST /api/auth/logout`
```json
{"refresh_token":"..."}
```

## Games

### `GET /api/games/search`
Сейчас принимает JSON body с фильтрами:
```json
{"name":"Root","offset":0,"limit":20}
```
Ответ:
```json
{"games":[{"id":1,"bgg_id":237182,"name":"Root","year_published":2018}],"next_offset":1,"has_next":false}
```

### `GET /api/games`
Получение нескольких игр. `server_id` — локальная игра, `bgg_id` — игра BGG.
```json
{
  "game_ids":[
    {"server_id":1},
    {"bgg_id":237182}
  ]
}
```
Ответ:
```json
{
  "games":[{
    "server_id":1,
    "bgg_id":237182,
    "name":"Root",
    "description":"...",
    "year_published":2018,
    "bgg_rating":null,
    "polka_rating":null,
    "best_count_players":[],
    "available_count_players":[2,3,4],
    "min_play_time_minutes":60,
    "max_play_time_minutes":90,
    "min_age":10,
    "weight":null
  }]
}
```

### `POST /api/games/create`
```json
{"name":"My game"}
```
Ответ:
```json
{"game_id":1,"name":"My game"}
```

## Collection

### `GET /api/collections/`
Возвращает игры коллекции текущего пользователя.

### `POST /api/collections/add`
```json
{"game_id":1}
```
Ответ: `{"message":"success"}`.

### `DELETE /api/collections/delete`
```json
{"game_id":1}
```

## Ошибки

Основные статусы: `400` — неверный запрос, `401` — нет/невалидный токен, `409` — конфликт, `500` — внутренняя ошибка.

## Что я бы изменил

1. **Не использовать JSON body в `GET`.** `/games/search` перевести на query-параметры (`?name=Root&offset=0&limit=20`) либо сделать `POST /games/search`, если фильтров много.
2. **`GET /games` сейчас имеет побочный эффект:** при `bgg_id` игра загружается из BGG и сохраняется в БД. Такой endpoint лучше сделать `POST /games/resolve` (или `/games/import`), а обычное чтение локальной игры — `GET /games/:id`.
3. **`POST /games/create` → `POST /games`.** HTTP-метод уже означает создание.
4. **Коллекцию сделать ресурсной:** `GET /collection`, `POST /collection/games`, `DELETE /collection/games/:game_id` вместо `/add` и `/delete`.
5. **Не возвращать `model.Game` прямо из `GET /collections/`.** Сделать DTO со стабильными `snake_case` JSON-полями, как в остальных endpoint'ах.
