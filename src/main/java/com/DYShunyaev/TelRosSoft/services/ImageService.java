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

    private Image toEntity(MultipartFile file) throws IOException {
        return Image.builder()
                .name(file.getName())
                .originalName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .bytes(file.getBytes())
                .build();
    }

    public Image getImage(long imageId) {
        return imageRepository.findById(imageId).orElseThrow();
    }

    public Image saveImage(MultipartFile file) throws IOException {
        Image image = toEntity(file);
        return imageRepository.save(image);
    }

    public Image updateImage(MultipartFile file, long imageId) throws IOException {
        Image image = toEntity(file);
        image.setId(imageId);
        return imageRepository.save(image);
    }

    public void deleteImageById(long imageId) {
        imageRepository.deleteById(imageId);
    }
}
