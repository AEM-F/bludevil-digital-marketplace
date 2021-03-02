package nl.fhict.digitalmarketplace.repository.product;

import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPlatformRepository extends PagingAndSortingRepository<ProductPlatform, Integer> {
    ProductPlatform getById(Integer id);
    ProductPlatform getProductPlatformByName(String name);
}
