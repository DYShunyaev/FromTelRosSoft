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

    /**
     * @saveUser(): Позволяет сохранить пользователя в БД, единственный параметр, который принимает данная функция,
     * это объект класса Users.
     * **/
    public void saveUser(Users users) {
        usersRepository.save(users);
    }
    /**
     * @updateUser(): Позволяет изменить данные пользователя в БД, на вход принимает id объект класса Users.
     * По id находим в БД пользователя, после чего проверяем параметры принятого объекта, если есть новые данные,
     * то перезаписываем их в имеющуюся сущност, после чего перезаписываем ее.
     * **/
    public Users updateUser(long userId, Users users) {
        Users users1 = usersRepository.findById(userId).orElseThrow();
        if (users.getUsername() != null) users1.setUsername(users.getUsername());
        if (users.getPassword() != null) users1.setPassword(users.getPassword());
        usersRepository.save(users1);
        return users1;
    }
    /**
     * @deleteUser(): Позволяет удалить данные пользователя в БД, на вход принимает id класса Users.
     * Т.к. UsersDetails принадлежат классу Users, нужно убедиться, что у данной модели отсутсвуют связи,
     * в противном случае удалить и их.
     * **/
    public void deleteUserById(long userId) {
        UsersDetails detailsId = usersRepository.findById(userId).orElseThrow()
                .getUsersDetails();
        if (detailsId != null) {
            usersDetailsRepository.deleteById(detailsId.getDetailsId());
        }
        usersRepository.deleteById(userId);
    }
    /**
     * @findAllUsers(): Возвращает список всех пользоваиелей, находящихся в Бд.
     * **/
    public List<Users> findAllUsers() {
        return (List<Users>) usersRepository.findAll();
    }
    /**
     * @findUserById(): Возвращает пользователя, по его id.
     * **/
    public Optional<Users> findUserById(long userId) {
        return usersRepository.findById(userId);
    }
    /**
     * @existUserById(): Проверяет наличие пользователя в БД, по его id.
     * **/
    public boolean existUserById(long userId) {
        return usersRepository.existsById(userId);
    }
}
