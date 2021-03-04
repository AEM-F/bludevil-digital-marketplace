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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class GenreService implements IGenreService {
    private Logger LOG = LoggerFactory.getLogger(GenreService.class);
    private GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre createGenre(@Valid Genre genre) throws ExistingResourceException {
        genre.setGenreName(genre.getGenreName().toLowerCase(Locale.ROOT));
        LOG.info("Checking if the genre with the given name exists");
        Genre foundGenre = genreRepository.getByGenreNameIgnoreCase(genre.getGenreName());
        if(foundGenre == null){
            LOG.info("Success, genre name is not in use");
            LOG.info("Saving genre...");
            return genreRepository.save(genre);
        }
        throw new ExistingResourceException("Genre name already exists");
    }

    @Override
    public List<Genre> getAllGenres() throws ResourceNotFoundException {
        List<Genre> genres = new ArrayList<>();
        genres = genreRepository.findAll();
        if(!genres.isEmpty()){
            return genres;
        }
        throw new ResourceNotFoundException("The are no genres");
    }

    @Override
    public Page<Genre> getGenres(int page, int size) throws ResourceNotFoundException, InvalidInputException {
        LOG.info("Validating input");
        if(page > 0){
            if(size > 0){
                Pageable requestedPage = PageRequest.of(page-1, size);
                Page<Genre> genres = genreRepository.findAll(requestedPage);
                if(genres.getContent().size() != 0){
                    LOG.info("Successfully returned the genres");
                    return genres;
                }
                throw new ResourceNotFoundException("No genres were found");
            }
            throw new InvalidInputException("The given size is not valid");
        }
        throw new InvalidInputException("The given page number is not valid");
    }

    @Override
    public Genre getById(Integer id) throws ResourceNotFoundException, InvalidInputException {
        LOG.info("Validating the given id: "+id);
        if(id > 0){
            LOG.info("Getting the genre with given id: "+id);
            Genre foundGenre = genreRepository.getById(id);
            if(foundGenre != null){
                LOG.info("Successfully returned the genre");
                return foundGenre;
            }
            throw new ResourceNotFoundException("The genre with the given id was not found");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public Genre updateGenre(@Valid Genre genre, Integer id) throws ExistingResourceException, ResourceNotFoundException, InvalidInputException {
        LOG.info("Validating given id: "+id);
        if(id > 0){
            LOG.info("Id is valid, checking if the genre exists");
            Genre foundGenre = genreRepository.getById(id);
            if (foundGenre != null){
                LOG.info("Found genre: "+foundGenre.toString());
                LOG.info("Checking if the genre name is used");
                Genre foundGenreByName = genreRepository.getByGenreNameIgnoreCase(genre.getGenreName().toLowerCase(Locale.ROOT));
                if(foundGenreByName == null){
                    LOG.info("No genres with the given name was found, attempting to update the genre");
                    foundGenre.setGenreName(genre.getGenreName().toLowerCase(Locale.ROOT));
                    genreRepository.save(foundGenre);
                    LOG.info("Successfully, updated the genre");
                    return foundGenre;
                }
                throw new ExistingResourceException("The given genre name is in use");
            }
            throw new ResourceNotFoundException("No genre was found with the given id");
        }
        throw new InvalidInputException("The given id is not valid");
    }
}
