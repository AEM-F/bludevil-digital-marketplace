package nl.fhict.digitalmarketplace.controller.image;

import nl.fhict.digitalmarketplace.customException.FileException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import nl.fhict.digitalmarketplace.service.img.IImageService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping(value = "api/images/")
public class ImageServiceController {
    public IImageService imageService;

    public ImageServiceController(IImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(path ={"upload"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> uploadImage(@RequestParam MultipartFile file) throws InvalidInputException, FileException {
        String response = imageService.uploadFile(file);
        return ResponseEntity.ok(response);
    }
    @GetMapping(
            path = {"getImage/{imageName:.+}"},
            produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE,MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> getImageWithMediaType(@PathVariable(name = "imageName") String fileName) throws FileException, ResourceNotFoundException, InvalidInputException, IOException {
        byte[] response = imageService.getFileWithMediaType(fileName);
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = {"upload/product/{id}"},
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
}
