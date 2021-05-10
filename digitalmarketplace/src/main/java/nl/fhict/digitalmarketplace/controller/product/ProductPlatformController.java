package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.enums.RetrievalMode;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.model.response.ValidityCheckResponse;
import nl.fhict.digitalmarketplace.service.enums.StringToRetrievalEnumConverter;
import nl.fhict.digitalmarketplace.service.product.IProductPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/productPlatforms")
public class ProductPlatformController {

    private IProductPlatformService platformService;

    @Autowired
    public ProductPlatformController(IProductPlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getPlatformById(@PathVariable(name = "id") Integer id) throws InvalidInputException, ResourceNotFoundException {
        ProductPlatform returnedPlatform = platformService.getPlatformById(id);
        return ResponseEntity.ok(returnedPlatform);
    }

    @GetMapping()
    public ResponseEntity<Object> getPlatforms(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(name = "retrieval", defaultValue = "PAGE") String mode) throws InvalidInputException, ResourceNotFoundException {
        StringToRetrievalEnumConverter enumConverter = new StringToRetrievalEnumConverter();
        RetrievalMode retrievalMode = enumConverter.convert(mode);
        if(retrievalMode != null){
            if(retrievalMode == RetrievalMode.PAGE){
                Page<ProductPlatform> platforms = platformService.getPlatforms(page, size);
                PaginationResponse<ProductPlatform> paginationResponse = new PaginationResponse<>(platforms.getContent(), platforms.getTotalElements(), platforms.getNumber()+1,platforms.getSize());
                return ResponseEntity.ok(paginationResponse);
            }
            else {
                List<ProductPlatform> returnedPlatforms;
                returnedPlatforms = platformService.getAllPlatforms();
                return ResponseEntity.ok(returnedPlatforms);
            }
        }
        throw new InvalidInputException("Conversion failed, the given retrieval mode is invalid");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createPlatform(@RequestBody ProductPlatform platform) throws ExistingResourceException {
        ProductPlatform createdPlatform = platformService.createProductPlatform(platform);
        return ResponseEntity.ok(createdPlatform);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updatePlatform(@RequestBody ProductPlatform platform,@PathVariable Integer id) throws InvalidInputException, ResourceNotFoundException, ExistingResourceException {
        ProductPlatform updatedPlatform = platformService.updatePlatform(platform, id);
        return ResponseEntity.ok(updatedPlatform);
    }

    @GetMapping(path="/checkNameValidity/{name}")
    public ResponseEntity<Object> checkPlatformNameValidity(@PathVariable(name = "name") String name) throws InvalidInputException {
        boolean isValidResult = platformService.checkNameValidity(name);
        ValidityCheckResponse responseBody = new ValidityCheckResponse(isValidResult);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(path = {"/search/{name}"})
    public ResponseEntity<Object> getPlatformsByName(@PathVariable(name = "name") String productName,@RequestParam(name = "page", defaultValue = "1") int page,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) throws InvalidInputException, ResourceNotFoundException {
        Page<ProductPlatform> platforms = platformService.getPlatformsByName(page, size,productName);
        PaginationResponse<ProductPlatform> paginationResponse = new PaginationResponse<>(platforms.getContent(), platforms.getTotalElements(), platforms.getNumber()+1,platforms.getSize());
        return ResponseEntity.ok(paginationResponse);
    }

}
