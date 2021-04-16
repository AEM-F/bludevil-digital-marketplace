package nl.fhict.digitalmarketplace.service.product.filter;

import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class ProductFilterSpec {
    private ProductRepository productRepository;

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductRepository getProductRepository(){
        return this.productRepository;
    }


    public abstract Page<Product> applyFilter(Pageable pageable, boolean productState);
}
