# REST-API Application with Java and Spring Framework

---
*Данный проект написан в соответствии с уловиями,
высланными на почту*

---

## 1) Описание проекта:
Проект представляет собой Spring Boot приложение,
с реализованной *CRUD* моделью и авторизацией. Программа позволяет 
создавать пользователей, получать и изменять информацию о них,
а так же загружать их изображения.

## 2) Стэк проекта:
- *[Java 17](https://openjdk.java.net/projects/jdk/17/)* язык программирования.
- *[Maven](https://maven.apache.org/)* сборщик Java проекта.
- *[Spring Framework](https://spring.io/)* дополнительный фрэймворк.
- *[Hibernate](https://hibernate.org/)* библиотека для работы с Базой данных.
- *[MySQL](https://www.mysql.com/)* База данных.
- *[IntelliJ IDEA](https://www.jetbrains.com/idea/)* IDE.

## 3) Запуск приложения:
Для запуска необходимо:

1. [Скачать](https://github.com/DYShunyaev/FromTelRosSoft.git) репозиторий.
2. Клонировать, т.е., загрузить копию *репозитория* на локальный диск компьютера.
```
https://github.com/DYShunyaev/FromTelRosSoft.git
```
3. Импортировать проект в [IntelliJ IDEA](https://www.jetbrains.com/idea/download/).
4. Сборка проекта `` mvn compile``
5. Перейти в класс [Main](src/main/java/com/DYShunyaev/TelRosSoft/TelRosSoftApplication.java)
6. Выполнить запуск.

## 4) Конфигурация:
Проект написан на основе паттерна **Model-View-Controller**
(далее MVC) для разделения данных приложения и управляющей логики.
---
### Models

---
Для достижения поставленных целей было реализовано 3 модели:
*[Users](src/main/java/com/DYShunyaev/TelRosSoft/models/Users.java),
[UsersDetails](src/main/java/com/DYShunyaev/TelRosSoft/models/UsersDetails.java),
[Image](src/main/java/com/DYShunyaev/TelRosSoft/models/Image.java)*.

#### *Users* содержит в себе 3 параметра:
```java
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id")
    private UsersDetails usersDetails;
    
    private String username;
    private String password;
}
```
Обязательный id, для хранения объекта в БД, поля username и password,
которые необходимы для авторизации,а так же объект UsersDetails,
помеченный аннотацией [OneToOne](https://www.baeldung.com/jpa-one-to-one),
для создания связи, между сущностями.

#### *UsersDetails*:

````java
public class UsersDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long detailsId;
    
    private String surname;
    private String name;
    private String patronymic;
    private Date birthday;
    private String email;
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Image image;
}
````

Для детальной информации, о пользователе, была создана отдельная Модель и таблица
в БД, чтобы была возможность для более гибкого расширения
функционала приложения и данных о пользователе, а так же разбиении
информации на основную (логин, пароль) и доплнительную. На этом же принципе 
основаны взаимоотношения UsersDetails -> Image. Поле помечено аннотацией
[OneToOne](https://www.baeldung.com/jpa-one-to-one).

#### *Image*:

````java
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String originalName;
    private String contentType;
    private Long size;

    @Lob
    private byte[] bytes;

}
````

Поля данного класса реализованны на основе той информации, 
которая поступает после принятие сервером изображения из формы.

---
### Repositories

---
Для каждой сущности созданы отдельные репозитории: 
*[UsersRepository](src/main/java/com/DYShunyaev/TelRosSoft/repositores/UsersRepository.java)*,
*[UsersDetailsRepository](src/main/java/com/DYShunyaev/TelRosSoft/repositores/UsersDetailsRepository.java)*,
*[ImageRepository](src/main/java/com/DYShunyaev/TelRosSoft/repositores/ImageRepository.java)*
для взаимодействия с БД. Основные операции [CRUD](https://ru.wikipedia.org/wiki/CRUD) уже реализованы
внутри класса, который наследует каждый репозиторий, их остается только переопределить в сервисе.
Так же, в данных классах присутствует возможность расширения функционала, путем создания
кастомных методов.

---
### Services

---
В данных классах хранится логика всего приложения, как и репозитрии, для кадой модели созданы отдельные 
классы Сервиса (*далее комментарии внутри кода*).
---
#### *[UsersService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java)*:

Данный класс содержит в себе методы, для работы с сущностью [Users](src/main/java/com/DYShunyaev/TelRosSoft/models/Users.java).

[@saveUser()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java):
Позволяет сохранить пользователя в БД, единственный параметр, который принимает данная функция, это объект класса Users.
```java
    public void saveUser(Users users) {
        usersRepository.save(users);
    }
```
[@updateUser()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java):
Позволяет изменить данные пользователя в БД, на вход принимает id объект класса Users. 
По id находим в БД пользователя, после чего проверяем параметры принятого объекта, 
если есть новые данные, то перезаписываем их в имеющуюся сущност, после чего перезаписываем ее.
```java
    public Users updateUser(long userId, Users users) {
        Users users1 = usersRepository.findById(userId).orElseThrow();
        if (users.getUsername() != null) users1.setUsername(users.getUsername());
        if (users.getPassword() != null) users1.setPassword(users.getPassword());
        usersRepository.save(users1);
        return users1;
    }
```
[@deleteUser()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java):
Позволяет удалить данные пользователя в БД, на вход принимает id класса Users. 
Т.к. UsersDetails принадлежат классу Users, нужно убедиться, что у данной модели 
отсутсвуют связи, в противном случае удалить и их.
```java
    public void deleteUserById(long userId) {
        UsersDetails detailsId = usersRepository.findById(userId).orElseThrow()
                .getUsersDetails();
        if (detailsId != null) {
            usersDetailsRepository.deleteById(detailsId.getDetailsId());
        }
        usersRepository.deleteById(userId);
    }
```
[@findAllUsers()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java):
Возвращает список всех пользоваиелей, находящихся в Бд.
```java
    public List<Users> findAllUsers() {
        return (List<Users>) usersRepository.findAll();
    }
```
[@findUserById()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java):
Возвращает пользователя, по его id.
```java
    public Optional<Users> findUserById(long userId) {
        return usersRepository.findById(userId);
    }
```
[@existUserById()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java):
Проверяет наличие пользователя в БД, по его id.
```java
    public boolean existUserById(long userId) {
        return usersRepository.existsById(userId);
    }
```
---
#### *[UsersDetailsService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java)*:

Данный класс содержит в себе методы, для работы с сущностью [UsersDetails](src/main/java/com/DYShunyaev/TelRosSoft/models/UsersDetails.java).

[@exsistUsersDetailsById()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java):
Проверяет наличие пользователя в БД, по id, после чего получает detailsId 
и выполняет проверку наличия детальной информации о пользователе.
```java
    public long exsistUsersDetailsById(long userId) {
        if (!usersService.existUserById(userId)) throw new NoSuchUsersException();
        long detailsId = usersService.findUserById(userId).orElseThrow().getUsersDetails().getDetailsId();
        if (!usersDetailsRepository.existsById(detailsId)) throw new NoSuchUsersException();
        return detailsId;
    }
```
[@getUsersDetailsById()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java):
Возвращает детальную информацию о пользователе, по id.
```java
    public Optional<UsersDetails> getUsersDetailsById(long id) {
        return usersDetailsRepository.findById(id);
    }
```
[@getUsersDetailsById()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java):
Позволяет удалить дополнительные данные пользователя в БД, на вход принимает id класса UsersDetails. 
Т.к. Image принадлежат классу UsersDetails, нужно убедиться, что у данной модели отсутсвуют связи, 
в противном случае удалить и их.
```java
    public void deleteUsersDetailsById(long id){
        Image image=usersDetailsRepository.findById(id).orElseThrow()
                    .getImage();
        if(image!=null){
            imageRepository.deleteById(image.getId());
        }
        usersDetailsRepository.deleteById(id);
    }
```
[@saveUsersDetails()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java):
Сохраняет в БД детальную информацию о пользователе.
```java
    public void saveUsersDetails(UsersDetails usersDetails) {
        usersDetailsRepository.save(usersDetails);
    }
```
[@updateUserDetails()](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java):
Позволяет изменить дополнительные данные пользователя в БД, на вход принимает id объект класса UsersDetails. 
По id находим в БД данные пользователя, после чего проверяем параметры принятого объекта, если есть новые данные,
то перезаписываем их в имеющуюся сущност, после чего перезаписываем ее.
```java
    public UsersDetails updateUserDetails(long detailsId, UsersDetails usersDetails) {
        UsersDetails details = usersDetailsRepository.findById(detailsId).orElseThrow();

        if (usersDetails.getSurname() != null) details.setSurname(usersDetails.getSurname());
        if (usersDetails.getName() != null) details.setName(usersDetails.getName());
        if (usersDetails.getPatronymic() != null) details.setPatronymic(usersDetails.getPatronymic());
        if (usersDetails.getBirthday() != null) details.setBirthday(usersDetails.getBirthday());
        if (usersDetails.getEmail() != null) details.setEmail(usersDetails.getEmail());
        if (usersDetails.getPhoneNumber() != null) details.setPhoneNumber(usersDetails.getPhoneNumber());

        usersDetailsRepository.save(details);
        return details;
    }
```
---
#### *[ImageService](src/main/java/com/DYShunyaev/TelRosSoft/services/ImageService.java)*:

[@toEntity()](src/main/java/com/DYShunyaev/TelRosSoft/services/ImageService.java):
Переопределяет объект File в модель Image, путем передачи всех параметров.
```java
    private Image toEntity(MultipartFile file) throws IOException {
        return Image.builder()
                .name(file.getName())
                .originalName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .bytes(file.getBytes())
                .build();
    }
```
[@getImage()](src/main/java/com/DYShunyaev/TelRosSoft/services/ImageService.java):
Возвращает данные из БД, по id.
```java
    public Image getImage(long imageId) {
        return imageRepository.findById(imageId).orElseThrow();
    }
```
[@saveImage()](src/main/java/com/DYShunyaev/TelRosSoft/services/ImageService.java):
Сохраняет объект в БД, путем преобразования его из File в Image, на вход принимает 
MultipartFile file.
```java
    public Image saveImage(MultipartFile file) throws IOException {
        Image image = toEntity(file);
        return imageRepository.save(image);
    }
```
[@updateImage()](src/main/java/com/DYShunyaev/TelRosSoft/services/ImageService.java):
Обновляет данные в БД, путем перезаписывания и присваивания уже существующего id.
```java
    public Image updateImage(MultipartFile file, long imageId) throws IOException {
        Image image = toEntity(file);
        image.setId(imageId);
        return imageRepository.save(image);
    }
```
[@deleteImageById()](src/main/java/com/DYShunyaev/TelRosSoft/services/ImageService.java):
Удаляет объект Image из БД, по его id.
```java
    public void deleteImageById(long imageId) {
        imageRepository.deleteById(imageId);
    }
```
---
### Controllers

---
В данном проекте реализовано 3 контролера, для каждой сущности. При этом
URL адреса связаны и идут от главной модели *[Users](src/main/java/com/DYShunyaev/TelRosSoft/models/Users.java)* 
до низшей, по иерархии, модели *[Image](src/main/java/com/DYShunyaev/TelRosSoft/models/Image.java)*.

---
#### *[MainController](src/main/java/com/DYShunyaev/TelRosSoft/controllers/MainController.java)*:

Реализует 5 методов, взаимодействующих с моделью [Users](src/main/java/com/DYShunyaev/TelRosSoft/models/Users.java).
Имеет 1 конструктор, в параметрах которого содержит 1 аргумент [UsersService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java), 
помечененный аннотацией [@Autowired](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html).

[@showAllUsers()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/MainController.java):
Принимает GET запрос, от клиентского сервиса, после чего возвращает List, содержащий всех Users, находящихся в БД.
```java
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Users> showAllUsers() {
        return usersService.findAllUsers();
    }
```
[@showUserById()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/MainController.java):
Принимает GET запрос, с параметром "userId", от клиентского сервиса, после чего проверяет, 
существует ли данный объект в БД, при отсутствии выдает Exception, при наличии возвращает объект Users, по его id.
```java
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Optional<Users> showUserById(@PathVariable(name = "id") long id) {
        if (!usersService.existUserById(id)) {
            throw new NoSuchUsersException("There is no employee with ID = " + id
            + " in Database.");
        }
        return usersService.findUserById(id);
    }
```
[@newUsers()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/MainController.java):
Принимает POST запрос и объект класса Users, от клиентского сервиса, после чего сохраняет его в БД и возвращает клиенту.
```java
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> newUsers(@RequestBody Users users) {
        usersService.saveUser(users);
        return new ResponseEntity<>(users,HttpStatusCode.valueOf(200));
    }
```
[@updateUser()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/MainController.java):
Принимает PUT запрос, id клиента и объект типа Users, от клиентского сервиса, 
далее идет проверка на наличие данного пользователя, в БД, если он присутствует, 
то вызывается метод "updateUser(userId,users)".
```java
    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateUser(@PathVariable(name = "id") long userId,
                                             @RequestBody Users users) {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }
        users = usersService.updateUser(userId,users);

        return new ResponseEntity<>(users,HttpStatusCode.valueOf(200));
    }
```
[@deleteUserById()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/MainController.java):
Принимает DELETE запрос, с параметром "userId", от клиентского сервиса, после чего проверяет, 
существует ли данный объект в БД, при отсутствии выдает Exception, при наличии удаляет объект Users, по его id.
```java
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteUserById(@PathVariable(name = "id") long id) {
        if (!usersService.existUserById(id)) {
            throw new NoSuchUsersException();
        }
        usersService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
```
---
#### *[UsersDetailsController](src/main/java/com/DYShunyaev/TelRosSoft/controllers/UsersDetailsController.java)*:

Реализует 4 метода, взаимодействующих с моделью [UsersDetails](src/main/java/com/DYShunyaev/TelRosSoft/models/UsersDetails.java).
Имеет 1 конструктор, в параметрах которого содержит 2 аргумента: [UsersDetailsService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java),
[UsersService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java),
помечененный аннотацией [@Autowired](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html).

URL методов, которые принимают параметр "id", принимают id модели [UsersDetailsService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java),
из которой уже получает id [UsersDetails](src/main/java/com/DYShunyaev/TelRosSoft/models/UsersDetails.java) 
объекта.

[@getUsersDetailsByUserId()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/UsersDetailsController.java):
Принимает GET запрос, с параметром "userId", от клиентского сервиса, после чего проверяет, существует ли данный объект в БД,
при отсутствии выдает Exception, при наличии возвращает объект UsersDetails, по его id.
```java
    @RequestMapping(value = "/users/details/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Optional<UsersDetails> getUsersDetailsByUserId(@PathVariable(name = "id")long userId) {
        long detailsId = usersDetailsService.exsistUsersDetailsById(userId);

        return usersDetailsService.getUsersDetailsById(detailsId);
    }
```
[@updateUsersDetailsByUsersId()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/UsersDetailsController.java):
Принимает PUT запрос, с параметром "userId" и объектом класса UsersDetails, от клиентского сервиса, после чего проверяет, 
существует ли данный объект в БД, при отсутствии выдает Exception, при получает detailsId и вызывает метод "updateUserDetails()",
возвращая измененный объект UsersDetails.
```java
    @RequestMapping(value = "/users/details/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateUsersDetailsByUsersId(@PathVariable(name = "id")long userId,
                                                              @RequestBody UsersDetails usersDetails) {
        long detailsId = usersDetailsService.exsistUsersDetailsById(userId);

        usersDetails = usersDetailsService.updateUserDetails(detailsId, usersDetails);

        return new ResponseEntity<>(usersDetails, HttpStatusCode.valueOf(200));
    }
```
[@saveUserDetails()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/UsersDetailsController.java):
Принимает POST запрос, с параметром "userId" и объектом класса UsersDetails, от клиентского сервиса, 
после чего проверяет, существует ли данный объект в БД, при отсутствии выдает Exception, 
при наличии присваевает пользователю новый объект класса UsersDetails и сохраняет его в БД.
```java
    @RequestMapping(value = "/users/details/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> saveUserDetails(@PathVariable(name = "id") long userId,
                                                  @RequestBody UsersDetails usersDetails) {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }
        Users users = usersService.findUserById(userId).orElseThrow();
        users.setUsersDetails(usersDetails);
        usersDetailsService.saveUsersDetails(usersDetails);
        return new ResponseEntity<>(users, HttpStatusCode.valueOf(200));
    }
```
[@deleteUserDetails()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/UsersDetailsController.java):
Принимает DELETE запрос, с параметром "userId", от клиентского сервиса, после чего проверяет,
существует ли данный объект в БД, при отсутствии выдает Exception, при наличии получает detailsId,
удаляет связь между объектами Users и UsersDetails, после чего удаляет детальную информацию пользователя из БД.
```java
    @RequestMapping(value = "/users/details/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteUserDetails(@PathVariable(name = "id") long userId) {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }
        long detailsId = usersService.findUserById(userId).orElseThrow()
                .getUsersDetails().getDetailsId();
        Users users = usersService.findUserById(userId).orElseThrow();
        users.setUsersDetails(null);
        usersService.updateUser(userId, users);
        usersDetailsService.deleteUsersDetailsById(detailsId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
```
---
#### *[ImageController](src/main/java/com/DYShunyaev/TelRosSoft/controllers/ImageController.java)*:
Реализует 4 метода, взаимодействующих с моделью [Image](src/main/java/com/DYShunyaev/TelRosSoft/models/Image.java).
Имеет 1 конструктор, в параметрах которого содержит 3 аргумента: [UsersDetailsService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java),
[UsersService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersService.java),
[ImageService](src/main/java/com/DYShunyaev/TelRosSoft/services/ImageService.java),
помечененный аннотацией [@Autowired](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html).

URL методов, которые принимают параметр "id", принимают id модели [UsersDetailsService](src/main/java/com/DYShunyaev/TelRosSoft/services/UsersDetailsService.java),
из которой получают id [UsersDetails](src/main/java/com/DYShunyaev/TelRosSoft/models/UsersDetails.java)
объекта, который содежит параметр объекта [Image](src/main/java/com/DYShunyaev/TelRosSoft/models/Image.java), из которого
берется "imageId".

[@getImage()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/ImageController.java):
Принимает GET запрос, с параметром "userId", от клиентского сервиса, после чего возвращает объект Image, по его id,
полученном из UsersDetails, который хранится в Users.
```java
    @GetMapping("")
    public ResponseEntity<?> getImage(@PathVariable(name = "userId")long userId) throws IOException {
        Image image = imageService.getImage(usersService.findUserById(userId).orElseThrow()
                .getUsersDetails().getImage().getId());

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
```
[@saveImage()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/ImageController.java):
Принимает POST запрос, с параметром "userId" и объектом класса MultipartFile, от клиентского сервиса, 
после чего присваевает парметр Image объекту usersDetails и сохраняет изображение, в виде модели класса Image, в БД.
```java
    @PostMapping("")
    public ResponseEntity<?> saveImage(@PathVariable(name = "userId")long userId,
                                       @RequestParam("file")MultipartFile file) throws IOException {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }

        Image image = imageService.saveImage(file);
        UsersDetails details = usersDetailsService.getUsersDetailsById(usersService.findUserById(userId).orElseThrow()
                .getUsersDetails().getDetailsId()).orElseThrow();
        details.setImage(image);
        usersDetailsService.updateUserDetails(details.getDetailsId(),details);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
```
[@updateImage()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/ImageController.java):
Принимает PUT запрос, с параметром "userId" и объектом класса MultipartFile, от клиентского сервиса, 
после чего получает detailsId и вызывает метод "updateImage()", возвращая измененное изображение.
```java
    @PutMapping("")
    public ResponseEntity<?> updateImage(@PathVariable(name = "userId")long userId,
                                       @RequestParam("file")MultipartFile file) throws IOException {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }

        Image image = imageService.updateImage(file,usersService.findUserById(userId).orElseThrow()
                .getUsersDetails().getImage().getId());

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
```
[@deleteImage()](src/main/java/com/DYShunyaev/TelRosSoft/controllers/ImageController.java):
Принимает DELETE запрос, с параметром "userId", от клиентского сервиса, получает imageId, 
удаляет связь между объектами UsersDetails и Image, после чего удаляет изображение пользователя из БД, 
возвращая объект UsersDetails.
```java
    @DeleteMapping("")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "userId")long userId) {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }
        UsersDetails details = usersDetailsService.getUsersDetailsById(usersService.findUserById(userId).orElseThrow()
                .getUsersDetails().getDetailsId()).orElseThrow();
        long imageId = details.getImage().getId();
        details.setImage(null);
        usersDetailsService.updateUserDetails(details.getDetailsId(),details);

        imageService.deleteImageById(imageId);

        return new ResponseEntity<>(details, HttpStatusCode.valueOf(200));
    }
```
---
### Security (Authorization)

---
За безопасность отвечает класс [BasicConfig](src/main/java/com/DYShunyaev/TelRosSoft/config/BasicConfig.java). Аунтефикация реализована на основе Базовой Авторизации, с хранением учетных записей в памяти и кодировкой пароля, 
в виде Password Encoder.

[@userDetailsService()](src/main/java/com/DYShunyaev/TelRosSoft/config/BasicConfig.java):
Обеспечивает создание и хранение 2х учетных записей, для авторизации.

```java
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
```

[@securityFilterChain()](src/main/java/com/DYShunyaev/TelRosSoft/config/BasicConfig.java):
Определяет цепочку фильтров, которую можно сопоставить с файлом HttpServletRequest, для того, чтобы решить, 
применимо ли оно к этому запросу.

```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/api/**").hasAnyRole("USER","ADMIN")
                    .anyRequest().authenticated()
                )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
```
[@passwordEncoder()](src/main/java/com/DYShunyaev/TelRosSoft/config/BasicConfig.java):
Предназначен для кодировки паролей.

```java
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
```
---

