package com.DYShunyaev.TelRosSoft.controllers;

import com.DYShunyaev.TelRosSoft.exception.NoSuchUsersException;
import com.DYShunyaev.TelRosSoft.models.Users;
import com.DYShunyaev.TelRosSoft.models.UsersDetails;
import com.DYShunyaev.TelRosSoft.services.UsersDetailsService;
import com.DYShunyaev.TelRosSoft.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsersDetailsController {

    private final UsersDetailsService usersDetailsService;
    private final UsersService usersService;

    @Autowired
    public UsersDetailsController(UsersDetailsService usersDetailsService, UsersService usersService) {
        this.usersDetailsService = usersDetailsService;
        this.usersService = usersService;
    }

    @RequestMapping(value = "/users/details/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Optional<UsersDetails> getUsersDetailsByUserId(@PathVariable(name = "id")long userId) {
        long detailsId = usersDetailsService.exsistUsersDetailsById(userId);

        return usersDetailsService.getUsersDetailsById(detailsId);
    }

    @RequestMapping(value = "/users/details/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateUsersDetailsByUsersId(@PathVariable(name = "id")long userId,
                                                              @RequestBody UsersDetails usersDetails) {
        long detailsId = usersDetailsService.exsistUsersDetailsById(userId);

        usersDetails = usersDetailsService.updateUserDetails(detailsId, usersDetails);

        return new ResponseEntity<>(usersDetails, HttpStatusCode.valueOf(200));
    }

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
}
