package nl.fhict.digitalmarketplace.service.product.filter;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductFilterService implements IProductFilterService{
    private ProductRepository productRepository;
    private Logger log = LoggerFactory.getLogger(ProductFilterService.class);

    public ProductFilterService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
   public Page<Product> filterBy(int page, int size, ProductFilterSpec productFilterSpec, boolean productState) throws ResourceNotFoundException, InvalidInputException {
       if(page > 0){
           if(size > 0){
               Pageable requestedPage = PageRequest.of(page-1, size);
               productFilterSpec.setProductRepository(productRepository);
               Page<Product> products = productFilterSpec.applyFilter(requestedPage, productState);
               if(!products.getContent().isEmpty()){
                   log.info("Successfully returned the products");
                   return products;
               }
               throw new ResourceNotFoundException("No products were found");
           }
           throw new InvalidInputException("The given size is not valid");
       }
       throw new InvalidInputException("The given page is not valid");
    }
}
