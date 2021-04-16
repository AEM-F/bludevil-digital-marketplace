package nl.fhict.digitalmarketplace.repository.product;

import nl.fhict.digitalmarketplace.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllByIsActiveAndProductPlatform_Name(boolean state, String platformName, Pageable pageable);
    Page<Product> findAllByIsActiveAndPrice(boolean state, double price, Pageable pageable);
    Page<Product> findAllByIsActiveAndProductPlatform_NameAndPrice(boolean state, String platformName, double price, Pageable pageable);
    Page<Product> findAllByNameIsContainingIgnoreCase(String productName, Pageable pageable);
    Page<Product> findAllByIsActive(boolean state, Pageable pageable);
    Product getById(Integer id);
    Product save(Product product);
    long count();
    long countAllByProductPlatform_Name(String platformName);
}
