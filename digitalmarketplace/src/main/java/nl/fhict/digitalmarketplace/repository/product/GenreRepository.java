package nl.fhict.digitalmarketplace.repository.product;

import nl.fhict.digitalmarketplace.model.product.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends PagingAndSortingRepository<Genre, Integer> {
    Genre getById(Integer id);
    Genre getByGenreNameIgnoreCase(String name);
    List<Genre> findAll();
}
