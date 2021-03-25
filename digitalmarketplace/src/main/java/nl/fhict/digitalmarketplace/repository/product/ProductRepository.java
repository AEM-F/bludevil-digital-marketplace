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
    Page<Product> findAllByProductPlatform_Name(String platformName, Pageable pageable);
    Page<Product> findAllByPrice(double price, Pageable pageable);
    Page<Product> findAllByProductPlatform_NameAndPrice(String platformName, double price, Pageable pageable);
    Page<Product> findAllByNameIsContainingIgnoreCase(String productName, Pageable pageable);
    Product getById(Integer id);
    @Modifying
    @Query("update Product p set p.isActive = 'false' where p.id = :productId")
    void deleteById(@Param("productId") Integer id);
//    void deleteById(Integer id);
    Product save(Product product);
    long count();
    long countAllByProductPlatform_Name(String platformName);
}
