## Постановка задачи
Необходимо реализовать API по созданию товаров, приемок, отгрузок и расчету
прибыльности. Работу с данными (отправка и получение) организовать в формате JSON.
К реализованному API составить небольшую документацию - список возможных команд с
передаваемыми в них параметрами и возможными ответами на запрос. 

## Описание
API, реализующий следующие возможности:
- Создание товара
- Приемка товара
- Отгрузка товара
- Расчет прибыли

Swagger документация содержится в корне репозитория.

### Работа с базой данный
 Приложение использует для работы СУБД `PostgreSQL`. 
Параметры базы данных:
- имя базы данных : `"moy-sklad-api"`
- имя пользователя : `"postgres"`
- пароль : `"root"`

## Запуск приложения
### Запуск с момощью Maven
```
$ mvn clean package
$ java -jar target/moy-sklad-api-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```