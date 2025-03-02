# Subscription Service

## Описание проекта
Микросервис на **Spring Boot 3**, предоставляющий **REST API** для управления пользователями и их подписками на цифровые сервисы.

Проект использует **Java 17** и **PostgreSQL**.  
Развертывается с помощью **Docker** и **docker-compose**.

## Функциональность

### 1️ API для управления пользователями
- **Создание пользователя**: `POST /users`
- **Получение информации о пользователе**: `GET /users/{id}`
- **Обновление данных пользователя**: `PUT /users/{id}`
- **Удаление пользователя**: `DELETE /users/{id}`
- **Получение списка всех пользователей**: `GET /users`

### 2️ API для подписок
- **Добавление подписки пользователю**: `POST /subscriptions/add?userId={id}&subscriptionId={id}`
- **Получение списка подписок пользователя**: `GET /subscriptions/user/{id}`
- **Удаление подписки**: `DELETE /subscriptions/remove?userId={id}&subscriptionId={id}`
- **Получение списка всех доступных подписок**: `GET /subscriptions/available`
- **Получение ТОП-3 популярных подписок**: `GET /subscriptions/top3`

### 3️ Интеграция с базой данных (PostgreSQL)
- Используется **Liquibase** для управления миграциями БД.
- Таблицы:
  - `users` (пользователи)
  - `available_subscriptions` (список доступных подписок)
  - `subscriptions` (связь пользователя с подпиской)

### 4️ Логирование
- Логирование выполнено через **SLF4J**.
- Логи записываются при вызове сервисных методов.

### 5️ Документирование API
- **Swagger OpenAPI**: доступен по адресу `/swagger-ui/index.html`.

---

## Установка и запуск

### 1. Локальный запуск (без Docker)
1. Убедитесь, что установлен **Java 17** и **PostgreSQL**.
2. Создайте базу данных `subscription_service` в PostgreSQL.
3. Настройте `application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/subscription_service
       username: your_db_user
       password: your_db_password
```
4. Запустите приложение: `mvn spring-boot:run`

### 2. Запуск с Docker
1. Запустите сервис с помощью docker-compose `docker-compose up -d`
2. API будет доступно по адресу `http://localhost:8080`

## Тестирование

Для тестирования контроллеров используются MockMvc и интеграционные тесты.
Запуск тестов: `mvn test`

## Структура проекта

```src/
 ├── main/
 │   ├── java/com/example/subscription/
 │   │   ├── controller/    # REST-контроллеры
 │   │   ├── service/       # Бизнес-логика
 │   │   ├── model/         # JPA-сущности
 │   │   ├── repository/    # Spring Data JPA
 │   │   ├── dto/           # Data Transfer Objects (DTO)
 │   │   ├── mapper/        # MapStruct мапперы
 │   │   ├── exception/     # Глобальный обработчик ошибок
 │   ├── resources/
 │   │   ├── application.yml    # Конфигурация Spring Boot
 │   │   ├── db/changelog/      # Миграции Liquibase
 ├── test/    # Тесты
```

## API Документация
После запуска, документация доступна по адресу:
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Стек технологий

    Java 17
    Spring Boot 3
    Spring Data JPA
    PostgreSQL
    Liquibase
    Swagger OpenAPI
    JUnit, Mockito
    Docker, Docker Compose
    SLF4J Logging
