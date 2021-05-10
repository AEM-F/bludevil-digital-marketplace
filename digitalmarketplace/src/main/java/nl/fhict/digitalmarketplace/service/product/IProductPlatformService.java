package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface IProductPlatformService {
    ProductPlatform createProductPlatform(@Valid ProductPlatform productPlatform) throws ExistingResourceException;
    ProductPlatform getPlatformById(Integer id) throws InvalidInputException, ResourceNotFoundException;
    ProductPlatform updatePlatform(@Valid ProductPlatform productPlatform, Integer id) throws InvalidInputException, ResourceNotFoundException, ExistingResourceException;
    Page<ProductPlatform> getPlatforms(int page, int size) throws InvalidInputException, ResourceNotFoundException;
    List<ProductPlatform> getAllPlatforms() throws ResourceNotFoundException;
    ProductPlatform getPlatformByName(String name) throws InvalidInputException, ResourceNotFoundException;
    boolean checkNameValidity(String name) throws InvalidInputException;
    Page<ProductPlatform> getPlatformsByName(int page, int size, String name) throws InvalidInputException, ResourceNotFoundException;
}
