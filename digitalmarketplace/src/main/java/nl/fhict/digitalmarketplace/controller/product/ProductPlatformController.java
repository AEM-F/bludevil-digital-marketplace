package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.service.product.IProductPlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/productPlatforms/")
public class ProductPlatformController {

    private Logger LOG = LoggerFactory.getLogger(ProductPlatformController.class);
    private IProductPlatformService platformService;

    @Autowired
    public ProductPlatformController(IProductPlatformService platformService) {
        this.platformService = platformService;
    }

    @RequestMapping(path = "{id}",method = RequestMethod.GET)
    public ResponseEntity getPlatformById(@PathVariable(name = "id") Integer id){
        ProductPlatform returnedPlatform = null;
        try {
            returnedPlatform = platformService.getPlatformById(id);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(returnedPlatform);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllPlatforms(){
        List<ProductPlatform> returnedPlatforms = new ArrayList<>();
        try {
            returnedPlatforms = platformService.getAllPlatforms();
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

        return ResponseEntity.ok(returnedPlatforms);
    }

    @RequestMapping(path = "pageable/{page}/{size}", method = RequestMethod.GET)
    public ResponseEntity getPlatforms(@PathVariable(name = "page") int page,@PathVariable(name = "size") int size){
        try {
            Page<ProductPlatform> platforms = platformService.getPlatforms(page, size);
            PaginationResponse<ProductPlatform> paginationResponse = new PaginationResponse<ProductPlatform>(platforms.getContent(), platforms.getTotalPages(), platforms.getNumber()+1,platforms.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
        catch (InvalidInputException | NumberFormatException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPlatform(@RequestBody ProductPlatform platform){
        try {
            ProductPlatform createdPlatform = platformService.createProductPlatform(platform);
            return ResponseEntity.ok(createdPlatform);
        }
        catch (ExistingResourceException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
    }

    @RequestMapping(path = "{id}",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePlatform(@RequestBody ProductPlatform platform,@PathVariable Integer id){
        try {
            ProductPlatform updatedPlatform = platformService.updatePlatform(platform, id);
            return ResponseEntity.ok(updatedPlatform);
        }
        catch (ExistingResourceException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

}
