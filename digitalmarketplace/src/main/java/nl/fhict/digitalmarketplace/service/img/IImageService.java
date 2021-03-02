package nl.fhict.digitalmarketplace.service.img;

import nl.fhict.digitalmarketplace.customException.FileException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface IImageService {
    String uploadFile(MultipartFile multipartFile) throws InvalidInputException, FileException;
    byte[] getFileWithMediaType(String fileName) throws InvalidInputException, IOException, FileException, ResourceNotFoundException;
}
