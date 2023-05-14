package com.DYShunyaev.TelRosSoft.controllers;

import com.DYShunyaev.TelRosSoft.exception.NoSuchUsersException;
import com.DYShunyaev.TelRosSoft.models.Users;
import com.DYShunyaev.TelRosSoft.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MainController {

    private final UsersService usersService;

    @Autowired
    public MainController(UsersService usersService) {
        this.usersService = usersService;
    }
    /**
     * @showAllUsers(): Принимает GET запрос, от клиентского сервиса, после чего возвращает List, содержащий
     * всех Users, находящихся в БД.
     * **/
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Users> showAllUsers() {
        return usersService.findAllUsers();
    }
    /**
     * @showUserById(): Принимает GET запрос, с параметром "userId", от клиентского сервиса, после чего проверяет, существует
     * ли данный объект в БД, при отсутствии выдает Exception, при наличии возвращает объект Users, по его id.
     * **/
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Optional<Users> showUserById(@PathVariable(name = "id") long id) {
        if (!usersService.existUserById(id)) {
            throw new NoSuchUsersException("There is no employee with ID = " + id
            + " in Database.");
        }
        return usersService.findUserById(id);
    }
    /**
     * @newUsers(): Принимает POST запрос и объект класса Users, от клиентского сервиса, после чего сохраняет его в
     * БД и возвращает клиенту.
     * **/
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> newUsers(@RequestBody Users users) {
        usersService.saveUser(users);
        return new ResponseEntity<>(users,HttpStatusCode.valueOf(200));
    }
    /**
     * @updateUser(): Принимает PUT запрос, id клиента и объект типа Users, от клиентского сервиса, далее идет проверка
     * на наличие данного пользователя, в БД, если он присутствует, то вызывается метод "updateUser(userId,users)".
     * **/
    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateUser(@PathVariable(name = "id") long userId,
                                             @RequestBody Users users) {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }
        users = usersService.updateUser(userId,users);

        return new ResponseEntity<>(users,HttpStatusCode.valueOf(200));
    }
    /**
     * @deleteUserById(): Принимает DELETE запрос, с параметром "userId", от клиентского сервиса, после чего проверяет,
     * существует ли данный объект в БД, при отсутствии выдает Exception, при наличии удаляет объект Users, по его id.
     * **/
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteUserById(@PathVariable(name = "id") long id) {
        if (!usersService.existUserById(id)) {
            throw new NoSuchUsersException();
        }
        usersService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
