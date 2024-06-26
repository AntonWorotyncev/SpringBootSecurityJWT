**SpringBootSecurityJWT**

 Базовое веб-приложение с использованием Spring Security и JWT для аутентификации и авторизации пользователей.

 Фyнкциональность:

  1. Поддержка ролей пользователей и авторизация на основе ролей;
  2. Хранение пользовательских данных в базе данных PostgresQL;
  3. Создание записи пользователя и генерации JWT токена при успешной аутентификации.
  4. API для аутентификации и регистрации пользователей.
     
Приложение имеет модульные тесты для контроллеров и сервисов.

Ход выполнения:

Для корректной работы приложения , необходимо выполнить команду``` docker compose up``` и развернуть контейнер 
с базой данных PostgresQL.

Далее , необходимо запустить приложение и перейти по адресу, чтобы увидеть стартовую страницу Swagger UI

```http://localhost:{your_port}/swagger-ui/index.html#/ ```

Там можно просмотреть более подробное описание конечных точек разработанного API. Здесь будет представлена базовая справка.

Существует два типа контроллеров:

1.```AuthController```  для регистрации  и авторизации пользователя;

```POST /sign-up``` -  обрабатывает запросы на регистрацию новых пользователей. Принимает данные регистрации в теле запроса, валидирует их и регистрирует пользователя, возвращая JWT-токен для дальнейшей аутентификации.

```POST /sign-in```- обрабатывает запросы на авторизацию пользователей. Принимает данные для входа в теле запроса, валидирует их и выполняет аутентификацию, возвращая JWT-токен для подтверждения успешного входа.
Получить роль ADMIN (в качестве теста и демонстрации)

```GET /get-admin``` - используется для получения роли администратора, для демонстрационных целей. Не принимает параметры и вызывает соответствующую службу для выполнения этой операции.


2.```ContentController``` - представляющий собой ресурс, с огранниченным доступом. 

```GET /resource``` -  Возвращает секретный контент для всех авторизованных пользователей.

```GET /resource/admin/``` - Возвращает секретный контент только для авторизованных пользователей с ролью администратора.

Принципиально удобным является возможность сохранить сгенерированный токен для добавления к заголовкам в последующих запросах.
 





  
