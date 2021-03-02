package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface IProductPlatformService {
    ProductPlatform createProductPlatform(@Valid ProductPlatform productPlatform) throws ExistingResourceException;
    ProductPlatform getPlatformById(Integer id) throws InvalidInputException, ResourceNotFoundException;
    ProductPlatform updatePlatform(@Valid ProductPlatform productPlatform, Integer id) throws InvalidInputException, ResourceNotFoundException, ExistingResourceException;
}
