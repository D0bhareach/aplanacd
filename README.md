### Техническое задание

Необходимо написать web-приложение для автоматизации обмена BlueRay дисками. Есть коллекция дисков у каждого
участника. Диски можно брать и отдавать. В системе есть три сущности: Disk (BlueRay-диск), User, TakenItem
(связка User-Disk).

#### В системе должно быть пять страниц:

авторизация, заведение нового диска, список свободных дисков (у всех
пользователей не взятые), список дисков, взятых пользователем, и список дисков, взятых у пользователя с
указанием кто взял. Диск можно взять и отдать (без денежных расчетов).

*** Технические требования:
  * Java 8
  * СУБД –любая. Предпочтительно использовать встроенную в приложение, например, hsqldb.
  * Работа с БД – Hibernate. Обязательно использование HQL или Criteria Builder.
  * Проект должен быть написан с использованием Spring Boot.
  HTML представление на клиентской части можно делать с помощью JSP, либо на чистом HTML. Использование
  js-фреймворков будет большим плюсом, например, Angular, AngularJS, ReactJS, VueJS или на крайний случай
  JQuery.  В оформлении клиентской части достаточно показать базовые навыки использования CSS. Использование
  CSS-фреймворков, например, Bootstrap, будет большим плюсом.
  * Проект должен собираться с помощью Maven или
  Gradle. Если приложение будет запускаться с помощью одной консольной команды и не требовать установки
  чего-либо еще – это будет большим плюсом.
  * Java код должен быть покрыт модульными тестами.  Результат
  необходимо выложить на github (или аналог), в readme файлe описать процедуру развертывания, установки и
  настройки приложения.


В задании описаны минимальные требования. Если вы знаете какие-то дополнительные
технологии / решения – покажите это. Например, можно дополнительно реализовать регистрацию новых
пользователей, используя Spring Security; подключить внешний ресурс в качестве источника списка существующих
фильмов; сделать локализацию GUI для русс. и англ. языков и т. д.  Качество сделанного тестового задания – это
главный показатель того как вы сможете в дальнейшем работать и как будете относиться к своей работе. 

### Project
Project is written with Spring Boot, Maven and H2 as in memory Database. Front end on React with ReactRouter.

### RESTful mapping:

#### User:

##### @GetMapping("user/")
return all users 
curl -v localhost:8080/user
##### @GetMapping("user/2")
return user with id = 2
curl -v localhost:8080/user/1

#### Disk

#### @GetMapping("disk/")
return all disks available and taken
curl -v localhost:8080/disk

#### @GetMapping("disk/1")
return disk with id = 1
curl -v localhost:8080/disk/1

#### @GetMapping("disk/available")
return all disks that are not taken
curl -v localhost:8080/disk/available

#### @GetMapping("disk/rent/from/user/3")
return all disks that are rented and owner is user with id = 3
curl localhost:8080/disk/rented/from/user/3

#### @GetMapping("disk/rent/by/user/3")
return all disks that are taken by user with id = 3
curl localhost:8080/disk/rented/by/user/3

#### @PutMapping("/disk/3")
return disk with new values and save new disk to database
curl -H 'Content-Type: application/json' -X PUT \
-d '{"name": "New name for disk # 3"}' \
localhost:8080/disk/3

#### @PostMapping("/disk/rent")
save TakenItem to database with userId and diskId provided in post req body 
return disk which is rented

curl -H 'Content-Type: application/json' -X POST \
-d '{"diskId": "5", "userId": "3"}' \
localhost:8080/disk/rent

#### @PostMapping("/disk")
save disk to database disk parameters values passed in req body as json
user need one parameter id if no user or id is wrong disk will be saved 
with default user.
curl -H 'Content-Type: application/json' -X POST \
-d '{"name": "new disk name", "company": "company of disk", "year": "2019", "user": {"id": "2"}}' \
localhost:8080/disk

#### @DeleteMapping("disk/2)
deletes disk with id = 2 from Disk table
curl -v -X DELETE localhost:8080/disk/6

#### @DeleteMapping("disk/return/6)
deletes disk with id = 6 from TakenItem table
curl -v -X DELETE localhost:8080/disk/return/6

#### Taken Item

####@GetMapping("/rented")
return list of taken items
curl -v localhost:8080/taken
