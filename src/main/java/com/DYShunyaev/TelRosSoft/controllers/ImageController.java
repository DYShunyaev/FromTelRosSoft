package com.DYShunyaev.TelRosSoft.controllers;

import com.DYShunyaev.TelRosSoft.exception.NoSuchUsersException;
import com.DYShunyaev.TelRosSoft.models.Image;
import com.DYShunyaev.TelRosSoft.models.UsersDetails;
import com.DYShunyaev.TelRosSoft.services.ImageService;
import com.DYShunyaev.TelRosSoft.services.UsersDetailsService;
import com.DYShunyaev.TelRosSoft.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/users/details/{userId}/img")
public class ImageController {

    private final ImageService imageService;
    private final UsersDetailsService usersDetailsService;
    private final UsersService usersService;

    @Autowired
    public ImageController(ImageService imageService, UsersDetailsService usersDetailsService, UsersService usersService) {
        this.imageService = imageService;
        this.usersDetailsService = usersDetailsService;
        this.usersService = usersService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable(name = "id")long imageId) throws IOException {
        Image image = imageService.getImage(imageId);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @PostMapping("")
    public ResponseEntity<?> saveImage(@PathVariable(name = "userId")long userId,
                                       @RequestParam("file")MultipartFile file) throws IOException {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }

        Image image = imageService.saveImage(file);
        UsersDetails details = usersDetailsService.getUsersDetailsById(usersService.findUserById(userId).orElseThrow()
                .getUsersDetails().getDetailsId()).orElseThrow();
        details.setImage(image);
        usersDetailsService.updateUserDetails(details.getDetailsId(),details);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateImage(@PathVariable(name = "userId")long userId,
                                       @PathVariable(name = "id")long imageId,
                                       @RequestParam("file")MultipartFile file) throws IOException {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }

        Image image = imageService.updateImage(file,imageId);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "userId")long userId,
                                         @PathVariable(name = "id")long imageId) {
        if (!usersService.existUserById(userId)) {
            throw new NoSuchUsersException();
        }
        UsersDetails details = usersDetailsService.getUsersDetailsById(usersService.findUserById(userId).orElseThrow()
                .getUsersDetails().getDetailsId()).orElseThrow();
        details.setImage(null);
        usersDetailsService.updateUserDetails(details.getDetailsId(),details);

        imageService.deleteImageById(imageId);

        return new ResponseEntity<>(details, HttpStatusCode.valueOf(200));
    }
}
