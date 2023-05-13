package com.DYShunyaev.TelRosSoft.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private Long id;

    private String name;

    private String originalName;

    private String contentType;

    private Long size;

    private byte[] bytes;
}
