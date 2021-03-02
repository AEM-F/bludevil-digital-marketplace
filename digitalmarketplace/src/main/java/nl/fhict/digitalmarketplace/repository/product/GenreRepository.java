package nl.fhict.digitalmarketplace.repository.product;

import nl.fhict.digitalmarketplace.model.product.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GenreRepository extends PagingAndSortingRepository<Genre, Integer> {
    Genre getById(Integer id);
    Genre getByGenreNameIgnoreCase(String name);
}
