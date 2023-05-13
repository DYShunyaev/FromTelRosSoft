package com.DYShunyaev.TelRosSoft.services;

import com.DYShunyaev.TelRosSoft.models.Users;
import com.DYShunyaev.TelRosSoft.models.UsersDetails;
import com.DYShunyaev.TelRosSoft.repositores.UsersDetailsRepository;
import com.DYShunyaev.TelRosSoft.repositores.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersDetailsRepository usersDetailsRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, UsersDetailsRepository usersDetailsRepository) {
        this.usersRepository = usersRepository;
        this.usersDetailsRepository = usersDetailsRepository;
    }

    public void saveUser(Users users) {
        usersRepository.save(users);
    }

    public Users updateUser(long userId, Users users) {
        Users users1 = usersRepository.findById(userId).orElseThrow();
        if (users.getUsername() != null) users1.setUsername(users.getUsername());
        if (users.getPassword() != null) users1.setPassword(users.getPassword());
        usersRepository.save(users1);
        return users1;
    }

    public void deleteUserById(long userId) {
        UsersDetails detailsId = usersRepository.findById(userId).orElseThrow()
                .getUsersDetails();
        if (detailsId != null) {
            usersDetailsRepository.deleteById(detailsId.getDetailsId());
        }
        usersRepository.deleteById(userId);
    }

    public List<Users> findAllUsers() {
        return (List<Users>) usersRepository.findAll();
    }

    public Optional<Users> findUserById(long userId) {
        return usersRepository.findById(userId);
    }

    public boolean existUserById(long userId) {
        return usersRepository.existsById(userId);
    }
}
