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

    @Lob
    private byte[] bytes;

}
