package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.service.product.GenreService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreServiceUnitTest {
    private Logger log = LoggerFactory.getLogger(GenreServiceUnitTest.class);
    private Genre testGenre;
    private List<Genre> testGenres;
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private GenreService genreService;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.openMocks(this);
        this.testGenre = new Genre(1, "action");
        List<Genre> testGenres = new ArrayList<>();
        testGenres.add(testGenre);
        testGenres.add(new Genre(2, "rpg"));
        testGenres.add(new Genre(3, "adventure"));
        this.testGenres = testGenres;
        log.info("[StartedTest]");
    }
    @After
    public void after(){
        log.info("[EndedTest]");
    }
    @Test
    public void createGenreTest() throws Exception{
        // arrange
        Genre insertGenre = new Genre("sandbox");
        Mockito.when(genreRepository.getByGenreNameIgnoreCase("sandbox")).thenReturn(null);
        Mockito.when(genreRepository.save(insertGenre)).thenReturn(insertGenre);
        // act
        Genre returnedGenre = genreService.createGenre(insertGenre);
        // assert
        String genreName = returnedGenre.getGenreName();
        boolean result = genreName.equals(insertGenre.getGenreName());
        assertTrue(result);
    }

    @Test(expected = ExistingResourceException.class)
    public void createGenreExistingNameTest() throws Exception{
        // arrange
        Genre insertGenre = new Genre("sandbox");
        Mockito.when(genreRepository.getByGenreNameIgnoreCase("sandbox")).thenReturn(insertGenre);
        // act
        Genre returnedGenre = genreService.createGenre(insertGenre);
        // assert
    }

    @Test
    public void getAllGenresTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.findAll()).thenReturn(testGenres);
        // act
        List<Genre> genres = genreService.getAllGenres();
        // assert
        int sizeReturned = genres.size();
        assertEquals(3, sizeReturned);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getAllGenresFailNoGenreTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.findAll()).thenReturn(new ArrayList<>());
        // act
        List<Genre> genres = genreService.getAllGenres();
        // assert
    }

    @Test
    public void getByIdTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.getById(1)).thenReturn(testGenre);
        // act
        Genre returnedGenre = genreService.getById(1);
        // assert
        String genreName = returnedGenre.getGenreName();
        boolean result = genreName.equals(testGenre.getGenreName());
        assertTrue(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByIdNotFoundTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.getById(1)).thenReturn(null);
        // act
        Genre returnedGenre = genreService.getById(1);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByIdNegativeIdTest() throws Exception{
        // arrange
        // act
        Genre returnedGenre = genreService.getById(-1);
        // assert
    }

    @Test
    public void updateGenreTest() throws Exception{
        // arrange
        Genre insertGenre = new Genre("sandbox");
        Mockito.when(genreRepository.getByGenreNameIgnoreCase("sandbox")).thenReturn(null);
        Mockito.when(genreRepository.getById(1)).thenReturn(testGenre);
        Mockito.when(genreRepository.save(insertGenre)).thenReturn(insertGenre);
        // act
        Genre returnedGenre = genreService.updateGenre(insertGenre, 1);
        // assert
        String genreName = returnedGenre.getGenreName();
        boolean result = genreName.equals(insertGenre.getGenreName());
        assertTrue(result);
    }

    @Test(expected = InvalidInputException.class)
    public void updateGenreNegativeIdTest() throws Exception{
        // arrange
        Genre insertGenre = new Genre("sandbox");
        // act
        Genre returnedGenre = genreService.updateGenre(insertGenre, -1);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateGenreWithNonExistingIdTest() throws Exception{
        // arrange
        Genre insertGenre = new Genre("sandbox");
        Mockito.when(genreRepository.getById(22)).thenReturn(null);
        // act
        Genre returnedGenre = genreService.updateGenre(insertGenre, 22);
        // assert
    }

    @Test(expected = ExistingResourceException.class)
    public void updateGenreWithExistingNameTest() throws Exception{
        // arrange
        Genre insertGenre = new Genre("action");
        Mockito.when(genreRepository.getById(2)).thenReturn(testGenres.get(2));
        Mockito.when(genreRepository.getByGenreNameIgnoreCase("action")).thenReturn(testGenre);
        // act
        Genre returnedGenre = genreService.updateGenre(insertGenre, 2);
        // assert
    }

    @Test
    public void getByNameTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.getByGenreNameIgnoreCase("action")).thenReturn(testGenre);
        // act
        Genre returnedGenre = genreService.getByName("action");
        // assert
        String genreName = returnedGenre.getGenreName();
        boolean result = genreName.equals("action");
        assertTrue(result);
    }

    @Test(expected = InvalidInputException.class)
    public void getByNameBlankStringTest() throws Exception{
        // arrange
        // act
        genreService.getByName("");
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByNameNotFoundTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.getByGenreNameIgnoreCase("sandbox")).thenReturn(null);
        // act
        genreService.getByName("sandbox");
        // assert
    }

    @Test
    public void checkNameValidityExistingNameTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.getByGenreNameIgnoreCase("action")).thenReturn(testGenre);
        // act
        boolean result = genreService.checkNameValidity("action");
        // assert
        assertFalse(result);
    }

    @Test(expected = InvalidInputException.class)
    public void checkNameValidityBlankNameTest() throws Exception{
        // arrange
        // act
        genreService.checkNameValidity("");
        // assert
    }

    @Test
    public void checkNameValidityTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.getByGenreNameIgnoreCase("sandbox")).thenReturn(null);
        // act
        boolean result = genreService.checkNameValidity("sandbox");
        // assert
        assertTrue(result);
    }

    @Test
    public void getByPageTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<Genre> genrePage = new Page<Genre>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 3;
            }

            @Override
            public <U> Page<U> map(Function<? super Genre, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 1;
            }

            @Override
            public int getSize() {
                return 3;
            }

            @Override
            public int getNumberOfElements() {
                return 3;
            }

            @Override
            public List<Genre> getContent() {
                return testGenres;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<Genre> iterator() {
                return null;
            }
        };
        Mockito.when(genreRepository.findAll(pageable)).thenReturn(genrePage);
        // act
        Page<Genre> genres = genreService.getGenres(1, 5);
        // assert
        int sizeReturned = genres.getContent().size();
        assertEquals(3, sizeReturned);
    }

    @Test(expected = InvalidInputException.class)
    public void getByPageNegativeSizeTest() throws Exception{
        // arrange
        // act
        genreService.getGenres(1, -1);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByPageNegativePageNrTest() throws Exception{
        // arrange
        // act
        genreService.getGenres(-1, 2);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByPageNoGenresTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<Genre> genrePage = new Page<Genre>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super Genre, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<Genre> getContent() {
                return new ArrayList<>();
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<Genre> iterator() {
                return null;
            }
        };
        Mockito.when(genreRepository.findAll(pageable)).thenReturn(genrePage);
        // act
        genreService.getGenres(1,5);
        // assert
    }

    @Test
    public void getByNamePageTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<Genre> genrePage = new Page<Genre>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 1;
            }

            @Override
            public <U> Page<U> map(Function<? super Genre, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 1;
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public int getNumberOfElements() {
                return 1;
            }

            @Override
            public List<Genre> getContent() {
               List<Genre> genres = new ArrayList<>();
               genres.add(testGenre);
                return genres;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<Genre> iterator() {
                return null;
            }
        };
        Mockito.when(genreRepository.findAllByGenreNameIsContainingIgnoreCase("action", pageable)).thenReturn(genrePage);
        // act
        Page<Genre> genres = genreService.getGenresByName(1, 5, "action");
        // assert
        int sizeReturned = genres.getContent().size();
        assertEquals(1, sizeReturned);
    }

    @Test(expected = InvalidInputException.class)
    public void getByNamePageNegativeSizeTest() throws Exception{
        // arrange
        // act
        genreService.getGenresByName(1, -1, "action");
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByNamePageNegativePageNrTest() throws Exception{
        // arrange
        // act
        genreService.getGenresByName(-1, 2, "action");
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByNamePageNoGenresTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<Genre> genrePage = new Page<Genre>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super Genre, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 1;
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<Genre> getContent() {
                List<Genre> genres = new ArrayList<>();
                return genres;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<Genre> iterator() {
                return null;
            }
        };
        Mockito.when(genreRepository.findAllByGenreNameIsContainingIgnoreCase("sandbox" ,pageable)).thenReturn(genrePage);
        // act
        genreService.getGenresByName(1,5, "sandbox");
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public  void getByNamePageInvalidNameTest() throws Exception{
        // arrange
        // act
        genreService.getGenresByName(1, 5, "");
        // assert
    }
}
