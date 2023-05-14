package com.DYShunyaev.TelRosSoft.services;

import com.DYShunyaev.TelRosSoft.models.Image;
import com.DYShunyaev.TelRosSoft.repositores.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    /**
     * @toEntity(): Переопределяет объект File в модель Image, путем передачи всех параметров.
     * **/
    private Image toEntity(MultipartFile file) throws IOException {
        return Image.builder()
                .name(file.getName())
                .originalName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .bytes(file.getBytes())
                .build();
    }
    /**
     * @getImage(): Возвращает данные из БД, по id.
     * **/
    public Image getImage(long imageId) {
        return imageRepository.findById(imageId).orElseThrow();
    }
    /**
     * @saveImage(): Сохраняет объект в БД, путем преобразования его из File в Image,
     * на вход принимает MultipartFile file.
     * **/
    public Image saveImage(MultipartFile file) throws IOException {
        Image image = toEntity(file);
        return imageRepository.save(image);
    }
    /**
     * @updateImage(): Обновляет данные в БД, путем перезаписывания и присваивания уже существующего id.
     * **/
    public Image updateImage(MultipartFile file, long imageId) throws IOException {
        Image image = toEntity(file);
        image.setId(imageId);
        return imageRepository.save(image);
    }
    /**
     * @deleteImageById(): Удаляет объект Image из БД, по его id.
     * **/
    public void deleteImageById(long imageId) {
        imageRepository.deleteById(imageId);
    }
}
