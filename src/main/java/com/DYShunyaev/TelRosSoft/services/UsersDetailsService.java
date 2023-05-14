package com.DYShunyaev.TelRosSoft.services;

import com.DYShunyaev.TelRosSoft.exception.NoSuchUsersException;
import com.DYShunyaev.TelRosSoft.models.Image;
import com.DYShunyaev.TelRosSoft.models.UsersDetails;
import com.DYShunyaev.TelRosSoft.repositores.ImageRepository;
import com.DYShunyaev.TelRosSoft.repositores.UsersDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UsersDetailsService {

    private final UsersDetailsRepository usersDetailsRepository;
    private final UsersService usersService;
    private final ImageRepository imageRepository;

    @Autowired
    public UsersDetailsService(UsersDetailsRepository usersDetailsRepository, UsersService usersService, ImageRepository imageRepository) {
        this.usersDetailsRepository = usersDetailsRepository;
        this.usersService = usersService;
        this.imageRepository = imageRepository;
    }
    /**
     * @exsistUsersDetailsById(): Проверяет наличие пользователя в БД, по id, после чего получает detailsId и выполняет
     * проверку наличия детальной информации о пользователе.
     * **/
    public long exsistUsersDetailsById(long userId) {
        if (!usersService.existUserById(userId)) throw new NoSuchUsersException();
        long detailsId = usersService.findUserById(userId).orElseThrow().getUsersDetails().getDetailsId();
        if (!usersDetailsRepository.existsById(detailsId)) throw new NoSuchUsersException();
        return detailsId;
    }
    /**
     * @getUsersDetailsById(): Возвращает детальную информацию о пользователе, по id.
     * **/
    public Optional<UsersDetails> getUsersDetailsById(long id) {
        return usersDetailsRepository.findById(id);
    }
    /**
     * @deleteUsersDetailsById(): Позволяет удалить дополнительные данные пользователя в БД,
     * на вход принимает id класса UsersDetails. Т.к. Image принадлежат классу UsersDetails, нужно убедиться,
     * что у данной модели отсутсвуют связи, в противном случае удалить и их.
     * **/
    public void deleteUsersDetailsById(long id) {
        Image image = usersDetailsRepository.findById(id).orElseThrow()
                        .getImage();
        if (image != null) {
            imageRepository.deleteById(image.getId());
        }
        usersDetailsRepository.deleteById(id);
    }
    /**
     * @saveUsersDetails(): Сохраняет в БД детальную информацию о пользователе.
     * **/
    public void saveUsersDetails(UsersDetails usersDetails) {
        usersDetailsRepository.save(usersDetails);
    }
    /**
     * @updateUserDetails(): Позволяет изменить дополнительные данные пользователя в БД,
     * на вход принимает id объект класса UsersDetails. По id находим в БД данные пользователя, после чего проверяем
     * параметры принятого объекта, если есть новые данные,то перезаписываем их в имеющуюся сущност, после чего перезаписываем ее.
     * **/
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
}
