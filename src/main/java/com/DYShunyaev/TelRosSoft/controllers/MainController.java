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

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Users> showAllUsers() {
        return usersService.findAllUsers();
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Optional<Users> showUserById(@PathVariable(name = "id") long id) {
        if (!usersService.existUserById(id)) {
            throw new NoSuchUsersException("There is no employee with ID = " + id
            + " in Database.");
        }
        return usersService.findUserById(id);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> newUsers(@RequestBody Users users) {
        usersService.saveUser(users);
        return new ResponseEntity<>(users,HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateUser(@PathVariable(name = "id") long userId,
                                             @RequestBody Users users) {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }
        users = usersService.updateUser(userId,users);

        return new ResponseEntity<>(users,HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteUserById(@PathVariable(name = "id") long id) {
        if (!usersService.existUserById(id)) {
            throw new NoSuchUsersException();
        }
        usersService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
