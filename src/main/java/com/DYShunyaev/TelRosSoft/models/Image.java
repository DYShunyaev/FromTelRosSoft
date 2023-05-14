package com.DYShunyaev.TelRosSoft.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String originalName;

    private String contentType;

    private Long size;

    /**
     * Возможна ошибка, при создании БД, колонке bytes присваивается значение TINYBLOB,
     * если imageTable.sql не сработает, то нужно вручную изменить тип на LONGBLOB**/
    @Lob
    private byte[] bytes;

}
