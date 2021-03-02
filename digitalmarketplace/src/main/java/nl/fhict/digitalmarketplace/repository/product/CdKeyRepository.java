package nl.fhict.digitalmarketplace.repository.product;

import nl.fhict.digitalmarketplace.model.product.CdKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CdKeyRepository extends PagingAndSortingRepository<CdKey, Integer> {
    Page<CdKey> findAllByProductId(Integer productId, Pageable pageable);
    Integer countAllByProductId(Integer productId);
}
