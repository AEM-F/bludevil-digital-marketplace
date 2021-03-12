package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface IProductService {
    Product getProductById(Integer id) throws InvalidInputException, ResourceNotFoundException;
    Product createProduct(@Valid Product product) throws InvalidInputException, ResourceNotFoundException;
    Product updateProduct(@Valid Product productToUpdate, Integer id) throws InvalidInputException, ResourceNotFoundException;
    Product deleteProductById(Integer id) throws InvalidInputException, ResourceNotFoundException;
    Page<Product> getProducts(int page, int size) throws InvalidInputException, ResourceNotFoundException;
//    Page<Product> getProductsByPrice(int page, int size, String price) throws InvalidInputException, ResourceNotFoundException;
//    Page<Product> getProductsByPriceAndPlatform(int page, int size, String price, String platformName) throws ResourceNotFoundException, InvalidInputException;
//    Page<Product> getProductsByPlatform(int page, int size, String platformName) throws InvalidInputException, ResourceNotFoundException;
    Page<Product> getProductsByName(int page, int size, String name) throws InvalidInputException, ResourceNotFoundException;
}
