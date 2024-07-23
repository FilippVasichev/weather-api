# Temperature Information Service

## Описание

Temperature Information Service — это REST-сервис, предназначенный для хранения и предоставления информации о
температуре в городах. Этот проект разработан с использованием Spring Boot и Java. Он позволяет запрашивать текущую и
историческую информацию о температуре для указанных городов, используя данные из нескольких публичных источников погоды.

## Основные особенности

- **Настройка городов и стран**: Список городов и стран задается в настройках сервиса.
- **Запрос температуры**: Температура для городов запрашивается из трех публично доступных REST-сервисов погоды.
- **Усреднение данных**: Значения температуры от разных сервисов усредняются и сохраняются в базе данных с текущим
  временным штампом.
- **Периодичность опроса**: Периодичность запроса данных задается в настройках приложения.
- **REST-ендпоинты**:
    - Получение всех имеющихся значений температуры для указанного города на определенную дату.
    - Если дата не указана, возвращается последнее известное значение температуры для города.

## Используемые технологии

- **Spring Boot**: 3.3
- **Java 21**
- **База данных**: Postgresql
- **Публичные API погоды**: OpenWeatherMap, WeatherAPI, YandexWeather.

## Настройки приложения

Конфигурация сервиса задается в файле `application.yml`:

```yaml
weather-api-settings: 
  # Интервал между запросами к API в миллисекундах (300000 ms = 5 минут)
  call-intervall
  # Задержка перед первым запуском после старта приложения в миллисекундах (50000 ms = 50 секунд)
  initial-delay

weather-api-keys:
  yandex: ${WEATHER_YANDEX_KEY}
  apiNinjas: ${WEATHER_NINJAS_KEY}
  openweathermap: ${WEATHER_OPENWEATHERMAP_KEY}
```

Ключи API можно задать через переменные окружения или заменить в файле `application.yaml`.

### Переменные окружения

Для работы сервиса необходимо задать следующие переменные окружения:

- `WEATHER_YANDEX_KEY` — твой ключ API для Яндекс Погоды
- `WEATHER_NINJAS_KEY` — твой ключ API для API Ninjas
- `WEATHER_OPENWEATHERMAP_KEY` — твой ключ API для OpenWeatherMap

#### Получение ключей API

- **Яндекс Погода**: [Получить ключ API](https://yandex.ru/pogoda/b2b/console/api-page)
- **API Ninjas**: [Получить ключ API](https://api-ninjas.com/profile)
- **OpenWeatherMap**: [Получить ключ API](https://home.openweathermap.org/api_keys)

## Установка и запуск

1. **Клонируйте репозиторий**:
    ```
    git clone git@github.com:FilippVasichev/weather-api.git
    ```
2. **Перейдите в директорию проекта**:

3. **Соберите проект**:
    ```
    ./gradlew clean build
    ```

4. **Создайте Docker-образ**:
    ```
    docker build -t weather-api-back .
    ```
5. **Создайте Docker-сеть:**

    ```
    docker network create weather-network
    ```

6. **Запустите контейнер с PostgreSQL**:
    ```
    docker run --name weather-api-db \
        --network weather-network \
        -e POSTGRES_USER=your-username \
        -e POSTGRES_PASSWORD=your-password \
        -e POSTGRES_DB=weather-db \
        -p 5432:5432 \
        -d postgres:15.4
    ```

7. **Запустите бэк приложения:**
    ```
    docker run --name weather-api-backend \
        --network weather-network \
        -e DB_HOST=jdbc:postgresql://weather-api-db:5432/weather-db \
        -e DB_USERNAME=your-username \
        -e DB_PASSWORD=your-password \
        -p 8080:8080 \
        -d weather-api-back
    ```

## Примеры запросов к API

### 1. Ручной запрос погоды

**Endpoint**: `/manually_fetch_weather`

**Метод**: `GET`

**Описание**: Запускает запрос погоды для всех городов из настроек и сохраняет результаты в базу данных.

**Пример запроса**:

```
curl -X GET http://localhost:8080/api/v1/weather/manually_fetch_weather
```

**Ответ:**

```json
"Weather saved successfully."
```

### 2. Получение погоды по стране и городу

**Endpoint**: `/get`

**Метод**: `GET`

**Описание**: Возвращает информацию о погоде для указанного города и страны. Если дата не указана, возвращается
последнее известное значение температуры.

**Параметры**:

- `country` (String): Название страны (например, `Russia`).
- `city` (String): Название города (например, `Moscow`).
- `date` (Date): Дата и время в формате ISO (например, `2024-07-23T10:33:00`). Если не
  указана, возвращается текущее значение температуры.

**Пример запроса**:

```
curl -X GET "http://localhost:8080/api/v1/weather/get?country=Russia&city=Moscow"
```

**Пример запроса с указанием даты:**

```
curl -X GET "http://localhost:8080/api/v1/weather/get?country=Russia&city=Moscow&date=2024-07-23T10:33:11"
```

**Ответ:**

```json lines
{
  "id": 1,
  "city": "Moscow",
  "country": "Russia",
  "temperature": 22,
  "date": "2024-07-23T10:33:00"
}
```

### 3. Получение всех записей о погоде

**Endpoint**: `/get/all`

**Метод**: `GET`

**Описание**: Возвращает список всех записей о погоде, сохраненных в базе данных. Эти данные включают информацию о
городе, стране, температуре и дате записи.

**Пример запроса**:

```bash
curl -X GET http://localhost:8080/api/v1/weather/get/all
```

**Ответ:**

```json lines
[
  {
    "id": 1,
    "city": "Moscow",
    "country": "Russia",
    "temperature": 22,
    "date": "2024-07-23T10:33:00"
  },
  {
    "id": 2,
    "city": "Berlin",
    "country": "Germany",
    "temperature": 18,
    "date": "2024-07-23T10:33:00"
  }
]
```