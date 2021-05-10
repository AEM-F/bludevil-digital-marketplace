package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.enums.RetrievalMode;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.model.response.ValidityCheckResponse;
import nl.fhict.digitalmarketplace.service.enums.StringToRetrievalEnumConverter;
import nl.fhict.digitalmarketplace.service.product.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/genres")
public class GenreController {

    private IGenreService genreService;

    @Autowired
    public GenreController(IGenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping()
    public ResponseEntity<Object> getGenres(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(name = "retrieval", defaultValue = "PAGE") String mode) throws InvalidInputException, ResourceNotFoundException {
        StringToRetrievalEnumConverter enumConverter = new StringToRetrievalEnumConverter();
        RetrievalMode retrievalMode = enumConverter.convert(mode);
        if(retrievalMode != null){
            if (retrievalMode == RetrievalMode.PAGE){
                Page<Genre> genres = genreService.getGenres(page, size);
                PaginationResponse<Genre> paginationResponse = new PaginationResponse<>(genres.getContent(), genres.getTotalElements(), genres.getNumber()+1,genres.getSize());
                return ResponseEntity.ok(paginationResponse);
            }
            else{
                List<Genre> genres;
                genres = genreService.getAllGenres();
                return ResponseEntity.ok(genres);
            }
        }
        throw new InvalidInputException("Conversion failed, the given retrieval mode is invalid");
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Object> getGenreById(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException, InvalidInputException {
        Genre returnedGenre = genreService.getById(id);
        return ResponseEntity.ok(returnedGenre);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateGenre(@RequestBody Genre genre,@PathVariable(name = "id") Integer id) throws InvalidInputException, ResourceNotFoundException, ExistingResourceException {
        Genre updatedGenre = genreService.updateGenre(genre, id);
        return ResponseEntity.ok(updatedGenre);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createGenre(@RequestBody Genre genre) throws ExistingResourceException {
        Genre createGenre = genreService.createGenre(genre);
        return ResponseEntity.ok(createGenre);
    }

    @GetMapping(path = "getByName/{name}")
    public ResponseEntity<Object> getGenreByName(@PathVariable(name = "name") String name) throws InvalidInputException, ResourceNotFoundException{
        Genre returnedGenre = genreService.getByName(name);
        return ResponseEntity.ok(returnedGenre);
    }

    @GetMapping(path="/checkNameValidity/{name}")
    public ResponseEntity<Object> checkGenreNameValidity(@PathVariable(name = "name") String name) throws InvalidInputException {
        boolean isValidResult = genreService.checkNameValidity(name);
        ValidityCheckResponse responseBody = new ValidityCheckResponse(isValidResult);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(path = {"/search/{name}"})
    public ResponseEntity<Object> getGenresByName(@PathVariable(name = "name") String productName,@RequestParam(name = "page", defaultValue = "1") int page,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) throws InvalidInputException, ResourceNotFoundException {
        Page<Genre> genres = genreService.getGenresByName(page, size,productName);
        PaginationResponse<Genre> paginationResponse = new PaginationResponse<>(genres.getContent(), genres.getTotalElements(), genres.getNumber()+1,genres.getSize());
        return ResponseEntity.ok(paginationResponse);
    }
}
