package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Genre;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface IGenreService {
    Genre createGenre(@Valid Genre genre) throws ExistingResourceException;
    List<Genre> getAllGenres() throws ResourceNotFoundException;
    Page<Genre> getGenres(int page, int size) throws ResourceNotFoundException, InvalidInputException;
    Genre getById(Integer id) throws ResourceNotFoundException, InvalidInputException;
    Genre updateGenre(@Valid Genre genre, Integer id) throws ExistingResourceException, ResourceNotFoundException, InvalidInputException;
}
