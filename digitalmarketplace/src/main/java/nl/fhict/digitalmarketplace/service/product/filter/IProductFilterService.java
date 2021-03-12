package nl.fhict.digitalmarketplace.service.product.filter;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import org.springframework.data.domain.Page;

public interface IProductFilterService {
    Page<Product> filterBy(int page, int size, ProductFilterSpec productFilterSpec) throws ResourceNotFoundException, InvalidInputException;
}
