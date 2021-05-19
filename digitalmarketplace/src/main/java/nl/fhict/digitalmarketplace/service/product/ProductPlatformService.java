package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.DigitalmarketplaceApplication;
import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Service
@Validated
public class ProductPlatformService implements IProductPlatformService {

    private Logger log = LoggerFactory.getLogger(ProductPlatformService.class);
    private ProductPlatformRepository productPlatformRepository;

    @Autowired
    public ProductPlatformService(ProductPlatformRepository productPlatformRepository) {
        this.productPlatformRepository = productPlatformRepository;
    }

    @Override
    public ProductPlatform createProductPlatform(@Valid ProductPlatform productPlatform) throws ExistingResourceException {
        productPlatform.setName(productPlatform.getName().toLowerCase(Locale.ROOT));
        log.info("Checking if platform with the given name exists");
        ProductPlatform foundPlatform = productPlatformRepository.getProductPlatformByNameIgnoreCase(productPlatform.getName());
        if(foundPlatform == null){
            log.info("Success, platform name does not exist");
            log.info("Saving product platform...");
            productPlatformRepository.save(productPlatform);
            return productPlatform;
        }
        throw new ExistingResourceException("Platform name already exists");
    }

    @Override
    public ProductPlatform getPlatformById(Integer id) throws InvalidInputException, ResourceNotFoundException {
        log.info("Validating the given id: "+id);
        if(id > 0){
            log.info("Getting the platform with given id: "+id);
            ProductPlatform foundPlatform = productPlatformRepository.getById(id);
            if(foundPlatform != null){
                log.info("Successfully returned the platform");
                return foundPlatform;
            }
            throw new ResourceNotFoundException("The platform with the given id was not found");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public ProductPlatform updatePlatform(@Valid ProductPlatform productPlatform, Integer id) throws InvalidInputException, ResourceNotFoundException, ExistingResourceException {
        log.info("Validating given id: "+id);
        if(id > 0){
            log.info("Id is valid, checking if the platform exists");
            ProductPlatform foundPlatform = productPlatformRepository.getById(id);
            if (foundPlatform != null){
                log.info("Found platform: "+foundPlatform.toString());
                log.info("Checking if the platform name is used");
                ProductPlatform foundPlatformByName = productPlatformRepository.getProductPlatformByNameIgnoreCase(productPlatform.getName().toLowerCase(Locale.ROOT));
                if(foundPlatformByName == null){
                    log.info("No platform with the given name was found, attempting to update platform");
                    foundPlatform.setName(productPlatform.getName().toLowerCase(Locale.ROOT));
                    productPlatformRepository.save(foundPlatform);
                    log.info("Successfully, updated platform");
                    return foundPlatform;
                }
                throw new ExistingResourceException("The given platform name is in use");
            }
            throw new ResourceNotFoundException("No platform was found with the given id");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public Page<ProductPlatform> getPlatforms(int page, int size) throws InvalidInputException, ResourceNotFoundException {
        log.info("Validating input");
        if(page > 0){
            if(size > 0){
                Pageable requestedPage = PageRequest.of(page-1, size);
                Page<ProductPlatform> platforms = productPlatformRepository.findAll(requestedPage);
                if(!platforms.getContent().isEmpty()){
                    log.info("Successfully returned the products");
                    return platforms;
                }
                throw new ResourceNotFoundException("No platforms were found,try adding some");
            }
            throw new InvalidInputException("The given size is not valid");
        }
        throw new InvalidInputException("The given page number is not valid");
    }

    @Override
    public List<ProductPlatform> getAllPlatforms() throws ResourceNotFoundException {
        List<ProductPlatform> platforms;
        platforms = productPlatformRepository.findAll();
        if(!platforms.isEmpty()){
            return platforms;
        }
        throw new ResourceNotFoundException("No platforms were found,try adding some");
    }

    @Override
    public ProductPlatform getPlatformByName(String name) throws InvalidInputException, ResourceNotFoundException {
        if (!DigitalmarketplaceApplication.isNullOrEmpty(name)){
            ProductPlatform foundPlatform = productPlatformRepository.getProductPlatformByNameIgnoreCase(name);
            if (foundPlatform != null){
                return foundPlatform;
            }
            throw new ResourceNotFoundException("No platform were found with the given name");
        }
        throw new InvalidInputException("Platform name cannot be empty");
    }

    @Override
    public boolean checkNameValidity(String name) throws InvalidInputException {
        if (!name.isBlank()){
            ProductPlatform foundPlatform = this.productPlatformRepository.getProductPlatformByNameIgnoreCase(name);
            if (foundPlatform == null){
                return true;
            }
            else{
                return false;
            }
        }
        throw new InvalidInputException("The given genre name is not valid");
    }

    @Override
    public Page<ProductPlatform> getPlatformsByName(int page, int size, String name) throws InvalidInputException, ResourceNotFoundException {
        if(!DigitalmarketplaceApplication.isNullOrEmpty(name)){
            if(page > 0){
                if(size > 0){
                    Pageable requestedPage = PageRequest.of(page-1, size);
                    Page<ProductPlatform> platforms = productPlatformRepository.findAllByNameIsContainingIgnoreCase(name, requestedPage);
                    if(!platforms.getContent().isEmpty()){
                        log.info("Successfully returned the platforms with the name: "+name);
                        return platforms;
                    }
                    throw new ResourceNotFoundException("No platforms were found");
                }
                throw new InvalidInputException("The given size is not valid");
            }
            throw new InvalidInputException("The given page is not valid");
        }
        throw new InvalidInputException("The given name can not be empty");
    }

}
