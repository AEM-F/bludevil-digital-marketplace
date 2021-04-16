package nl.fhict.digitalmarketplace.service.img;

import nl.fhict.digitalmarketplace.DigitalmarketplaceApplication;
import nl.fhict.digitalmarketplace.customException.FileException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService implements IImageService{

    private final String rootPath = System.getProperty("user.dir");
    private Logger log = LoggerFactory.getLogger(ImageService.class);

    @Override
    public String uploadFile(MultipartFile multipartFile) throws InvalidInputException, FileException {
        if(multipartFile != null && multipartFile.getOriginalFilename() != null){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            log.info(fileName);
            log.info("Creating directory to store file");
            File dir = new File(this.rootPath+File.separator+"images");
            if(!dir.exists()){
                log.info("Directory does not exists, creating one");
                dir.mkdirs();
            }
            log.info("Creating the file on the server");
            File serverFile = new File(dir.getAbsolutePath()+File.separator+fileName);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(multipartFile.getBytes());
            } catch (Exception e) {
                throw new FileException(e.getMessage());
            }
            log.info("Server file location: "+serverFile.getAbsolutePath());
            log.info("Creating response with url of the file");
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("api/images/getImage/").path(fileName).toUriString();
        }
        throw new InvalidInputException("The given file cannot be empty");
    }

    @Override
    public byte[] getFileWithMediaType(String fileName) throws InvalidInputException, ResourceNotFoundException, IOException {
        if(!DigitalmarketplaceApplication.isNullOrEmpty(fileName)){
                String storageDirPath = this.rootPath+"\\"+"images"+"\\"+fileName;
                Path destination = Paths.get(storageDirPath);
                if(Files.exists(destination)){
                    return Files.readAllBytes(destination);
                }
                throw new ResourceNotFoundException("No images found with the given name");
            }
        throw new InvalidInputException("The given file name can not be empty");
    }

    @Override
    public String uploadFileForProduct(MultipartFile multipartFile, Integer productId) throws FileException, InvalidInputException {
        if(multipartFile != null && productId > 0 && multipartFile.getOriginalFilename() != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            int lastIndexOf = fileName.lastIndexOf(".");
            if (lastIndexOf == -1) {
                throw new InvalidInputException("The given file doesn't have a valid extension");
            }
            String fileExt =  fileName.substring(lastIndexOf);
            String fileToSaveFullName = "product-image-"+productId+fileExt;
            log.info("Creating directory to store file");
            File dir = new File(this.rootPath+File.separator+"images");
            if(!dir.exists()){
                log.info("Directory does not exists, creating one");
                dir.mkdirs();
            }
            log.info("Creating the file on the server");
            File serverFile = new File(dir.getAbsolutePath()+File.separator+fileToSaveFullName);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(multipartFile.getBytes());
            } catch (Exception e) {
                throw new FileException(e.getMessage());
            }
            log.info("Server file location: "+serverFile.getAbsolutePath());
            log.info("Creating response with url of the file");
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("api/images/getImage/").path(fileToSaveFullName).toUriString();
        }
        throw new InvalidInputException("The given file and product id are invalid");
    }
}
