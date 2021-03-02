package nl.fhict.digitalmarketplace.controller.image;

import nl.fhict.digitalmarketplace.customException.FileException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import nl.fhict.digitalmarketplace.service.img.IImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "api/images/")
public class ImageServiceController {
    private Logger LOG = LoggerFactory.getLogger(ImageServiceController.class);
    public IImageService imageService;

    public ImageServiceController(IImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(path ={"upload"},method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity uploadImage(@RequestParam MultipartFile file){
        try {
            String response = imageService.uploadFile(file);
            return ResponseEntity.ok(response);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (FileException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }

    }
    @RequestMapping(
            path = {"getImage/{imageName:.+}"},
            method = RequestMethod.GET,
            produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE,MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity getImageWithMediaType(@PathVariable(name = "imageName") String fileName){
        try {
            byte[] response = imageService.getFileWithMediaType(fileName);
            return ResponseEntity.ok(response);
        }
        catch (InvalidInputException e){
            LOG.error("Exception msg: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (IOException e){
            LOG.error("Exception msg: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (ResourceNotFoundException | FileException e){
            LOG.error("Exception msg: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
