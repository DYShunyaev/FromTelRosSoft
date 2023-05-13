package com.DYShunyaev.TelRosSoft.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class UsersDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long detailsId;

    @Column
    private String surname;

    @Column
    private String name;

    @Column
    private String patronymic;

    @Column
    private Date birthday;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Image image;

    public UsersDetails() {
    }

    public UsersDetails(String surname, String name, String patronymic, Date birthday, String email, String phoneNumber) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UsersDetails{" +
                "detailsId=" + detailsId +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersDetails that = (UsersDetails) o;
        return detailsId == that.detailsId && Objects.equals(surname, that.surname) && Objects.equals(name, that.name) && Objects.equals(patronymic, that.patronymic) && Objects.equals(birthday, that.birthday) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(detailsId, surname, name, patronymic, birthday, email, phoneNumber);
    }
}
