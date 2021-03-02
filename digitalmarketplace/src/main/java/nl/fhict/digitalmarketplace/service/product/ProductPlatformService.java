package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Locale;

@Service
@Validated
public class ProductPlatformService implements IProductPlatformService {

    private Logger LOG = LoggerFactory.getLogger(ProductPlatformService.class);
    private ProductPlatformRepository productPlatformRepository;

    @Autowired
    public ProductPlatformService(ProductPlatformRepository productPlatformRepository) {
        this.productPlatformRepository = productPlatformRepository;
    }

    @Override
    public ProductPlatform createProductPlatform(@Valid ProductPlatform productPlatform) throws ExistingResourceException {
        productPlatform.setName(productPlatform.getName().toLowerCase(Locale.ROOT));
        LOG.info("Checking if platform name exists");
        ProductPlatform foundPlatform = productPlatformRepository.getProductPlatformByName(productPlatform.getName());
        if(foundPlatform == null){
            LOG.info("Success, platform name does exist");
            LOG.info("Saving product platform...");
            productPlatformRepository.save(productPlatform);
            return productPlatform;
        }
        throw new ExistingResourceException("Platform name already exists");
    }

    @Override
    public ProductPlatform getPlatformById(Integer id) throws InvalidInputException, ResourceNotFoundException {
        LOG.info("Validating the given id: "+id);
        if(id > 0){
            LOG.info("Getting the platform with given id: "+id);
            ProductPlatform foundPlatform = productPlatformRepository.getById(id);
            if(foundPlatform != null){
                LOG.info("Successfully returned the platform");
                return foundPlatform;
            }
            throw new ResourceNotFoundException("The platform with the given id was not found");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public ProductPlatform updatePlatform(@Valid ProductPlatform productPlatform, Integer id) throws InvalidInputException, ResourceNotFoundException, ExistingResourceException {
        LOG.info("Validating given id: "+id);
        if(id > 0){
            LOG.info("Id is valid, checking if the platform exists");
            ProductPlatform foundPlatform = productPlatformRepository.getById(id);
            if (foundPlatform != null){
                LOG.info("Found platform: "+foundPlatform.toString());
                LOG.info("Checking if the platform name is used");
                ProductPlatform foundPlatformByName = productPlatformRepository.getProductPlatformByName(productPlatform.getName().toLowerCase(Locale.ROOT));
                if(foundPlatformByName == null){
                    LOG.info("No platform with the given name was found, attempting to update platform");
                    foundPlatform.setName(productPlatform.getName().toLowerCase(Locale.ROOT));
                    productPlatformRepository.save(foundPlatform);
                    LOG.info("Successfully, updated platform");
                    return foundPlatform;
                }
                throw new ExistingResourceException("The given platform name is used");
            }
            throw new ResourceNotFoundException("No platform was found with the given id");
        }
        throw new InvalidInputException("The given id is not valid");
    }
}
