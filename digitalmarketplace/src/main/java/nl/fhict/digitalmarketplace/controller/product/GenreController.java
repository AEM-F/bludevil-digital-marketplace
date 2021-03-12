package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.enums.RetrievalMode;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.service.enums.StringToRetrievalEnumConverter;
import nl.fhict.digitalmarketplace.service.product.IGenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/genres")
public class GenreController {

    private Logger LOG = LoggerFactory.getLogger(GenreController.class);
    private IGenreService genreService;

    @Autowired
    public GenreController(IGenreService genreService) {
        this.genreService = genreService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getGenres(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(name = "retrieval", defaultValue = "PAGE") String mode){
        try {
            StringToRetrievalEnumConverter enumConverter = new StringToRetrievalEnumConverter();
            RetrievalMode retrievalMode = enumConverter.convert(mode);
            if(retrievalMode != null){
                switch (retrievalMode){
                    case ALL:{
                        List<Genre> genres;
                        genres = genreService.getAllGenres();
                        return ResponseEntity.ok(genres);
                    }
                    case PAGE:{
                        Page<Genre> genres = genreService.getGenres(page, size);
                        PaginationResponse<Genre> paginationResponse = new PaginationResponse<Genre>(genres.getContent(), genres.getTotalPages(), genres.getNumber()+1,genres.getSize());
                        return ResponseEntity.ok(paginationResponse);
                    }
                }
            }
            throw new InvalidInputException("Conversion failed, the given retrieval mode is invalid");
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @RequestMapping(path = {"/{id}"}, method = RequestMethod.GET)
    public ResponseEntity getGenreById(@PathVariable(name = "id") Integer id){
        Genre returnedGenre = null;
        try {
            returnedGenre = genreService.getById(id);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(returnedGenre);
    }

    @RequestMapping(path = "/{id}",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateGenre(@RequestBody Genre genre,@PathVariable(name = "id") Integer id){
        try {
            Genre updatedGenre = genreService.updateGenre(genre, id);
            return ResponseEntity.ok(updatedGenre);
        }
        catch (ExistingResourceException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @RequestMapping(path = {"/{id}"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createGenre(@RequestBody Genre genre){
        try {
            Genre createGenre = genreService.createGenre(genre);
            return ResponseEntity.ok(createGenre);
        }
        catch (ExistingResourceException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
    }
}
