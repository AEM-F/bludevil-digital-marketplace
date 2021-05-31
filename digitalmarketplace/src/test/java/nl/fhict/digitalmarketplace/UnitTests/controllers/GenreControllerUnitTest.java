package nl.fhict.digitalmarketplace.UnitTests.controllers;

import nl.fhict.digitalmarketplace.controller.product.GenreController;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.service.product.GenreService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreControllerUnitTest {
    private Genre testGenre;
    @Mock
    private GenreService genreService;
    @InjectMocks
    private GenreController genreController;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.openMocks(this);
        this.testGenre = new Genre(1, "action");
        List<Genre> testGenres = new ArrayList<>();
        testGenres.add(testGenre);
        testGenres.add(new Genre(2, "rpg"));
        testGenres.add(new Genre(3, "adventure"));
        Mockito.when(genreService.getById(1)).thenReturn(testGenre);
        Mockito.when(genreService.getAllGenres()).thenReturn(testGenres);
        Mockito.when(genreService.createGenre(testGenre)).thenReturn(testGenre);
        Mockito.when(genreService.getByName("origin")).thenReturn(testGenre);
        Mockito.when(genreService.updateGenre(testGenre, 1)).thenReturn(testGenre);
        Mockito.when(genreService.checkNameValidity("action")).thenReturn(true);
    }

    @Test
    public void getPlatformByIdTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = genreController.getGenreById(1);
        HttpStatus responseEntityStatusCode = responseEntity.getStatusCode();
        int status = responseEntityStatusCode.value();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void createProductPlatformTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = genreController.createGenre(this.testGenre);
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void updateProductPlatformTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = genreController.updateGenre(this.testGenre, 1);
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void getAllPlatformsTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = genreController.getGenres(1, 5, "all");
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void checkNameValidityTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = genreController.checkGenreNameValidity("action");
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }
}
