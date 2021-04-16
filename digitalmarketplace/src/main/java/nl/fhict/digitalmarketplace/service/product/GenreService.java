package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Service
public class GenreService implements IGenreService {
    private Logger log = LoggerFactory.getLogger(GenreService.class);
    private GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre createGenre(@Valid Genre genre) throws ExistingResourceException {
        genre.setGenreName(genre.getGenreName().toLowerCase(Locale.ROOT));
        log.info("Checking if the genre with the given name exists");
        Genre foundGenre = genreRepository.getByGenreNameIgnoreCase(genre.getGenreName());
        if(foundGenre == null){
            log.info("Success, genre name is not in use");
            log.info("Saving genre...");
            return genreRepository.save(genre);
        }
        throw new ExistingResourceException("Genre name already exists");
    }

    @Override
    public List<Genre> getAllGenres() throws ResourceNotFoundException {
        List<Genre> genres;
        genres = genreRepository.findAll();
        if(!genres.isEmpty()){
            return genres;
        }
        throw new ResourceNotFoundException("There are no genres, try adding some");
    }

    @Override
    public Page<Genre> getGenres(int page, int size) throws ResourceNotFoundException, InvalidInputException {
        log.info("Validating input");
        if(page > 0){
            if(size > 0){
                Pageable requestedPage = PageRequest.of(page-1, size);
                Page<Genre> genres = genreRepository.findAll(requestedPage);
                if(!genres.getContent().isEmpty()){
                    log.info("Successfully returned the genres");
                    return genres;
                }
                throw new ResourceNotFoundException("No genres were found, try adding some");
            }
            throw new InvalidInputException("The given size is not valid");
        }
        throw new InvalidInputException("The given page number is not valid");
    }

    @Override
    public Genre getById(Integer id) throws ResourceNotFoundException, InvalidInputException {
        log.info("Validating the given id: "+id);
        if(id > 0){
            log.info("Getting the genre with given id: "+id);
            Genre foundGenre = genreRepository.getById(id);
            if(foundGenre != null){
                log.info("Successfully returned the genre");
                return foundGenre;
            }
            throw new ResourceNotFoundException("The genre with the given id was not found");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public Genre updateGenre(@Valid Genre genre, Integer id) throws ExistingResourceException, ResourceNotFoundException, InvalidInputException {
        log.info("Validating given id: "+id);
        if(id > 0){
            log.info("Id is valid, checking if the genre exists");
            Genre foundGenre = genreRepository.getById(id);
            if (foundGenre != null){
                log.info("Found genre: "+foundGenre.toString());
                log.info("Checking if the genre name is used");
                Genre foundGenreByName = genreRepository.getByGenreNameIgnoreCase(genre.getGenreName().toLowerCase(Locale.ROOT));
                if(foundGenreByName == null){
                    log.info("No genres with the given name was found, attempting to update the genre");
                    foundGenre.setGenreName(genre.getGenreName().toLowerCase(Locale.ROOT));
                    genreRepository.save(foundGenre);
                    log.info("Successfully, updated the genre");
                    return foundGenre;
                }
                throw new ExistingResourceException("The given genre name is in use");
            }
            throw new ResourceNotFoundException("No genre was found with the given id");
        }
        throw new InvalidInputException("The given id is not valid");
    }
}
