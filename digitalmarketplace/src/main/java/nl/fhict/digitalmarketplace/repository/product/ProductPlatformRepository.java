package nl.fhict.digitalmarketplace.repository.product;

import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPlatformRepository extends PagingAndSortingRepository<ProductPlatform, Integer> {
    ProductPlatform getById(Integer id);
    ProductPlatform getProductPlatformByNameIgnoreCase(String name);
    List<ProductPlatform> findAll();
    Page<ProductPlatform> findAllByNameIsContainingIgnoreCase(String name, Pageable pageable);
}
