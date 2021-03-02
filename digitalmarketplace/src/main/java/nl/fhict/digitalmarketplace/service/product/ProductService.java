package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.DigitalmarketplaceApplication;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;


@Validated
@Service
public class ProductService implements IProductService{

    private ProductRepository productRepository;
    private ProductPlatformRepository productPlatformRepository;
    private Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductRepository productRepository, ProductPlatformRepository productPlatformRepository) {
        this.productRepository = productRepository;
        this.productPlatformRepository = productPlatformRepository;
    }

    @Override
    public Product getProductById(Integer id) throws InvalidInputException, ResourceNotFoundException {
        LOG.info("Validating the given id: "+id);
        if(id > 0){
            LOG.info("Getting the product with given id: "+id);
            Product foundProduct = productRepository.getById(id);
            if(foundProduct != null){
                LOG.info("Successfully returned the product");
                return foundProduct;
            }
            throw new ResourceNotFoundException("The product with the given id was not found");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public Product createProduct(@Valid Product product) throws InvalidInputException, ResourceNotFoundException {
        LOG.info("Validating product platform: "+product.getProductPlatform().toString());
        ProductPlatform productPlatform = productPlatformRepository.getById(product.getProductPlatform().getId());
        if(productPlatform != null){
            if(productPlatform.getName().equals(product.getProductPlatform().getName())){
                LOG.info("Saving product...");
                Product productCreated = productRepository.save(product);
                return productCreated;
            }
            throw new InvalidInputException("The given platform name does not match with the platform id");
        }
        throw new ResourceNotFoundException("No platform was found the given id");
    }

    @Override
    public Product updateProduct(@Valid Product productToUpdate, Integer id) throws InvalidInputException, ResourceNotFoundException {
        LOG.info("Validating the given id: "+id);
        if(id>0){
            Product foundProduct = productRepository.getById(id);
            if(foundProduct != null){
                LOG.info("Validating product platform: "+productToUpdate.getProductPlatform().toString());
                ProductPlatform productPlatform = productPlatformRepository.getById(productToUpdate.getProductPlatform().getId());
                if(productPlatform != null){
                    if(productPlatform.getName().equals(productToUpdate.getProductPlatform().getName())){
                        LOG.info("Updating the found product: "+foundProduct.toString());
                        productToUpdate.setId(foundProduct.getId());
                        productRepository.save(productToUpdate);
                        LOG.info("Finished updating the product");
                        return foundProduct;
                    }
                    throw new InvalidInputException("The given platform name does not match with the platform id");
                }
                throw new ResourceNotFoundException("No platform was found the given id");
            }
            throw new ResourceNotFoundException("No product was found with the given id");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public Product deleteProductById(Integer id) throws InvalidInputException, ResourceNotFoundException{
        LOG.info("Validating the given id: "+id);
        if(id > 0){
            Product foundProduct = productRepository.getById(id);
            if(foundProduct != null){
                productRepository.deleteById(id);
                LOG.info("Successfully deleted the product");
                return foundProduct;
            }
            throw new ResourceNotFoundException("No product was found with the given id");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public Page<Product> getProducts(int page, int size) throws InvalidInputException, ResourceNotFoundException {
        LOG.info("Validating input");
        if(page > 0){
            if(size > 0){
                Pageable requestedPage = PageRequest.of(page-1, size);
                Page<Product> products = productRepository.findAll(requestedPage);
                if(products.getContent().size() != 0){
                    LOG.info("Successfully returned the products");
                    return products;
                }
                throw new ResourceNotFoundException("No products were found");
            }
            throw new InvalidInputException("The given size is not valid");
        }
        throw new InvalidInputException("The given page number is not valid");

    }

    @Override
    public Page<Product> getProductsByPrice(int page, int size, String price) throws InvalidInputException, ResourceNotFoundException {
        if(!DigitalmarketplaceApplication.isNullOrEmpty(price)){
            double priceConverted = Double.parseDouble(price);
            if(priceConverted > 0.00){
                if(page > 0){
                    if(size > 0){
                        Pageable requestedPage = PageRequest.of(page-1, size);
                        Page<Product> products = productRepository.findAllByPrice(priceConverted, requestedPage);
                        if(products.getContent().size() != 0){
                            LOG.info("Successfully returned the products filtered by price");
                            return products;
                        }
                        throw new ResourceNotFoundException("No products were found");
                    }
                    throw new InvalidInputException("The given size is not valid");
                }
                throw new InvalidInputException("The given page is not valid");
            }
            throw new InvalidInputException("The given price is not valid");
        }
        throw new InvalidInputException("The given value is empty");
    }

    @Override
    public Page<Product> getProductsByPriceAndPlatform(int page, int size, String price, String platformName) throws ResourceNotFoundException, InvalidInputException {
        if(!DigitalmarketplaceApplication.isNullOrEmpty(price) && !DigitalmarketplaceApplication.isNullOrEmpty(platformName)){
            ProductPlatform foundPlatform = productPlatformRepository.getProductPlatformByName(platformName);
            if(foundPlatform != null){
                double priceConverted = Double.parseDouble(price);
                if(priceConverted > 0.00){
                    if(page > 0){
                        if(size > 0){
                            Pageable requestedPage = PageRequest.of(page-1, size);
                            Page<Product> products = productRepository.findAllByProductPlatform_NameAndPrice(foundPlatform.getName(), priceConverted, requestedPage);
                            if(products.getContent().size() != 0){
                                LOG.info("Successfully returned the products filtered by price and platform");
                                return products;
                            }
                            throw new ResourceNotFoundException("No products were found");
                        }
                        throw new InvalidInputException("The given size is not valid");
                    }
                    throw new InvalidInputException("The given page is not valid");
                }
                throw new InvalidInputException("The given price is not valid");
            }
            throw new InvalidInputException("The given platform name was not found");
        }
        throw new InvalidInputException("The given prince and platform can not be empty");
    }

    @Override
    public Page<Product> getProductsByPlatform(int page, int size, String platformName) throws InvalidInputException, ResourceNotFoundException {
        if(!DigitalmarketplaceApplication.isNullOrEmpty(platformName)){

            ProductPlatform foundPlatform = productPlatformRepository.getProductPlatformByName(platformName);
            if(foundPlatform != null){
                if(page > 0){
                    if(size > 0){
                        Pageable requestedPage = PageRequest.of(page-1, size);
                        Page<Product> products = productRepository.findAllByProductPlatform_Name(foundPlatform.getName(), requestedPage);
                        if(products.getContent().size() != 0){
                            LOG.info("Successfully returned the products filtered by platform");
                            return products;
                        }
                        throw new ResourceNotFoundException("No products were found");
                    }
                    throw new InvalidInputException("The given size is not valid");
                }
                throw new InvalidInputException("The given page is not valid");
            }
            throw new InvalidInputException("The given platform name was not found");
        }
        throw new InvalidInputException("The given platform can not be empty");
    }

    @Override
    public Page<Product> getProductsByName(int page, int size, String name) throws InvalidInputException, ResourceNotFoundException {
        if(!DigitalmarketplaceApplication.isNullOrEmpty(name)){
            if(page > 0){
                if(size > 0){
                    Pageable requestedPage = PageRequest.of(page-1, size);
                    Page<Product> products = productRepository.findAllByNameIsContainingIgnoreCase(name, requestedPage);
                    if(products.getContent().size() != 0){
                        LOG.info("Successfully returned the products with the name: "+name);
                        return products;
                    }
                    throw new ResourceNotFoundException("No products were found");
                }
                throw new InvalidInputException("The given size is not valid");
            }
            throw new InvalidInputException("The given page is not valid");
        }
        throw new InvalidInputException("The given name can not be empty");
        }

    }

