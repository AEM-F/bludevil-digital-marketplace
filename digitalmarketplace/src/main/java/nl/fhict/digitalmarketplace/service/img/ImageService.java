package nl.fhict.digitalmarketplace.service.img;

import nl.fhict.digitalmarketplace.DigitalmarketplaceApplication;
import nl.fhict.digitalmarketplace.customException.FileException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
    private Logger LOG = LoggerFactory.getLogger(ImageService.class);
    @Override
    public String uploadFile(MultipartFile multipartFile) throws InvalidInputException, FileException {
        if(!multipartFile.isEmpty()){
            try {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                LOG.info("Creating directory to store file");
                String rootPath = System.getProperty("user.dir");
                File dir = new File(rootPath+File.separator+"images");
                if(!dir.exists()){
                    LOG.info("Directory does not exists, creating one");
                    dir.mkdirs();
                }
                LOG.info("Creating the file on the server");
                File serverFile = new File(dir.getAbsolutePath()+File.separator+fileName);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(multipartFile.getBytes());
                stream.close();
                LOG.info("Server file location: "+serverFile.getAbsolutePath());
                LOG.info("Creating response with url of the file");
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/images/getImage/").path(fileName).toUriString();
                return fileDownloadUri;
            }catch (Exception e){
                throw new FileException(e.getMessage());
            }
        }
        throw new InvalidInputException("The given file cannot be empty");
    }

    @Override
    public byte[] getFileWithMediaType(String fileName) throws InvalidInputException, ResourceNotFoundException, IOException {
        if(!DigitalmarketplaceApplication.isNullOrEmpty(fileName)){
                String storageDirPath = System.getProperty("user.dir")+"\\"+"images"+"\\"+fileName;
                Path destination = Paths.get(storageDirPath);
                if(Files.exists(destination)){
                    byte[] foundImage = Files.readAllBytes(destination);
                    return foundImage;
                }
                throw new ResourceNotFoundException("No images found with the given name");
            }
        throw new InvalidInputException("The given file name can not be empty");
    }
}
