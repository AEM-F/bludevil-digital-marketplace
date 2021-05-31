package nl.fhict.digitalmarketplace.controller.image;

import nl.fhict.digitalmarketplace.customException.*;
import nl.fhict.digitalmarketplace.model.request.UpdateImageUrlRequest;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.service.img.IImageService;

import nl.fhict.digitalmarketplace.service.jwt.IRefreshTokenService;
import nl.fhict.digitalmarketplace.service.user.IUserService;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "api/images")
public class ImageServiceController {
    private IImageService imageService;
    private IRefreshTokenService refreshTokenService;
    private IUserService userService;

    public ImageServiceController(IImageService imageService, IRefreshTokenService refreshTokenService, IUserService userService) {
        this.imageService = imageService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path ={"/upload"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> uploadImage(@RequestParam MultipartFile file) throws InvalidInputException, FileException {
        String response = imageService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = {"/getImage/{imageName:.+}"},
            produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE,MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> getImageWithMediaType(@PathVariable(name = "imageName") String fileName) throws FileException, ResourceNotFoundException, InvalidInputException, IOException {
        byte[] response = imageService.getFileWithMediaType(fileName);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            path = {"/upload/product/{id}"},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> uploadImageForProduct(@RequestParam MultipartFile file, @PathVariable(name = "id") Integer id) throws InvalidInputException, FileException {
        String response = imageService.uploadFileForProduct(file, id);
        MessageDTO responseBody = new MessageDTO();
        responseBody.setType("IMAGE");
        responseBody.setMessage(response);
        return ResponseEntity.ok(responseBody);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path = {"/upload/user/{refreshToken}"},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> uploadImageByUser(@RequestParam MultipartFile file, @PathVariable(name = "refreshToken") String refreshToken) throws InvalidUUIDPatternException, TokenRefreshException, ResourceNotFoundException, InvalidInputException, FileException, IOException {
        User returnedUser = refreshTokenService.getUserInformationByToken(refreshToken);
        if (file.getOriginalFilename() == null) {
            throw new InvalidInputException("The given file doesn't have a name");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            throw new InvalidInputException("The given file doesn't have a valid extension");
        }
        String fileExt =  fileName.substring(lastIndexOf);
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        imageService.uploadImageForUser(IOUtils.toByteArray(inputStream), returnedUser.getId(), fileExt);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Request is under process");
        return ResponseEntity.ok(responseBody);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path = "/update/user")
    public ResponseEntity<Object> updateUserImageUrl(@Valid @RequestBody UpdateImageUrlRequest updateRequest) throws InvalidUUIDPatternException, TokenRefreshException, ResourceNotFoundException, InvalidInputException {
        User returnedUser = refreshTokenService.getUserInformationByToken(updateRequest.getRefreshToken());
        returnedUser.setImagePath(updateRequest.getImageUrl());
        userService.updateUser(returnedUser, returnedUser.getId());
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Request was successful");
        return ResponseEntity.ok(responseBody);
    }

}
