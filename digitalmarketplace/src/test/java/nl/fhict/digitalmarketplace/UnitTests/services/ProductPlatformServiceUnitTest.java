package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import nl.fhict.digitalmarketplace.service.product.ProductPlatformService;
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
public class ProductPlatformServiceUnitTest {
    private Logger log = LoggerFactory.getLogger(ProductPlatformServiceUnitTest.class);
    private ProductPlatform testPlatform;
    private List<ProductPlatform> testPlatforms;
    @Mock
    private ProductPlatformRepository platformRepository;
    @InjectMocks
    private ProductPlatformService platformService;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.openMocks(this);
        this.testPlatform = new ProductPlatform(1, "origin");
        List<ProductPlatform> testPlatforms = new ArrayList<>();
        testPlatforms.add(testPlatform);
        testPlatforms.add(new ProductPlatform(2, "steam"));
        testPlatforms.add(new ProductPlatform(3, "epic"));
        this.testPlatforms = testPlatforms;
        log.info("[StartedTest]");
    }

    @After
    public void after(){
        log.info("[EndedTest]");
    }
    @Test
    public void createPlatformTest() throws Exception{
        // arrange
        ProductPlatform platformToSave = new ProductPlatform("uplay");
        Mockito.when(platformRepository.getProductPlatformByNameIgnoreCase("uplay")).thenReturn(null);
        Mockito.when(platformRepository.save(platformToSave)).thenReturn(platformToSave);
        // act
        ProductPlatform returnedPlatform = platformService.createProductPlatform(platformToSave);
        // assert
        String platformName = returnedPlatform.getName();
        boolean result = platformName.equals(platformToSave.getName());
        assertTrue(result);
    }

    @Test(expected = ExistingResourceException.class)
    public void createPlatformExistingNameTest() throws Exception{
        // arrange
        ProductPlatform platformToAdd = new ProductPlatform("origin");
        Mockito.when(platformRepository.getProductPlatformByNameIgnoreCase("origin")).thenReturn(platformToAdd);
        // act
        ProductPlatform productPlatform = platformService.createProductPlatform(platformToAdd);
        // assert
    }

    @Test
    public void getAllPlatformsTest() throws Exception{
        // arrange
        Mockito.when(platformRepository.findAll()).thenReturn(testPlatforms);
        // act
        List<ProductPlatform> platforms = platformService.getAllPlatforms();
        // assert
        int sizeReturned = platforms.size();
        assertEquals(3, sizeReturned);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getAllPlatformsWithNoPlatformsTest() throws Exception{
        // arrange
        Mockito.when(platformRepository.findAll()).thenReturn(new ArrayList<>());
        // act
        List<ProductPlatform> platforms = platformService.getAllPlatforms();
        // assert
    }

    @Test
    public void getByIdTest() throws Exception{
        // arrange
        Mockito.when(platformRepository.getById(1)).thenReturn(testPlatform);
        // act
        ProductPlatform returnedPlatform = platformService.getPlatformById(1);
        // assert
        String platformName = returnedPlatform.getName();
        boolean result = platformName.equals(testPlatform.getName());
        assertTrue(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByIdNotFoundTest() throws Exception{
        // arrange
        Mockito.when(platformRepository.getById(1)).thenReturn(null);
        // act
        ProductPlatform returnedPlatform = platformService.getPlatformById(1);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByIdNegativeIdTest() throws Exception{
        // arrange
        // act
        ProductPlatform returnedPlatform = platformService.getPlatformById(-1);
        // assert
    }

    @Test
    public void updatePlatformTest() throws Exception{
        // arrange
        ProductPlatform platformToUpdate = new ProductPlatform("uplay");
        Mockito.when(platformRepository.getProductPlatformByNameIgnoreCase("uplay")).thenReturn(null);
        Mockito.when(platformRepository.getById(1)).thenReturn(testPlatform);
        Mockito.when(platformRepository.save(platformToUpdate)).thenReturn(platformToUpdate);
        // act
        ProductPlatform returnedPlatform = platformService.updatePlatform(platformToUpdate, 1);
        // assert
        String platformName = returnedPlatform.getName();
        boolean result = platformName.equals(platformToUpdate.getName());
        assertTrue(result);
    }

    @Test(expected = InvalidInputException.class)
    public void updatePlatformNegativeIdTest() throws Exception{
        // arrange
        ProductPlatform insertPlatform = new ProductPlatform("uplay");
        // act
        ProductPlatform productPlatform = platformService.updatePlatform(insertPlatform, -1);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updatePlatformWithNonExistingIdTest() throws Exception{
        // arrange
        ProductPlatform platformToUpdate = new ProductPlatform("uplay");
        Mockito.when(platformRepository.getById(22)).thenReturn(null);
        // act
        ProductPlatform returnedPlatform = platformService.updatePlatform(platformToUpdate, 22);
        // assert
    }

    @Test(expected = ExistingResourceException.class)
    public void updatePlatformWithExistingNameTest() throws Exception{
        // arrange
        ProductPlatform productPlatform1 = new ProductPlatform("origin");
        Mockito.when(platformRepository.getById(2)).thenReturn(testPlatforms.get(2));
        Mockito.when(platformRepository.getProductPlatformByNameIgnoreCase("origin")).thenReturn(testPlatform);
        // act
        ProductPlatform productPlatform = platformService.updatePlatform(productPlatform1, 2);
        // assert
    }

    @Test
    public void getByNameTest() throws Exception{
        // arrange
        Mockito.when(platformRepository.getProductPlatformByNameIgnoreCase("origin")).thenReturn(testPlatform);
        // act
        ProductPlatform platformByName = platformService.getPlatformByName("origin");
        // assert
        String platformName = platformByName.getName();
        boolean result = platformName.equals("origin");
        assertTrue(result);
    }

    @Test(expected = InvalidInputException.class)
    public void getByNameBlankStringTest() throws Exception{
        // arrange
        // act
        platformService.getPlatformByName("");
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByNameNotFoundTest() throws Exception{
        // arrange
        Mockito.when(platformRepository.getProductPlatformByNameIgnoreCase("wow")).thenReturn(null);
        // act
        platformService.getPlatformByName("wow");
        // assert
    }

    @Test
    public void checkNameValidityExistingNameTest() throws Exception{
        // arrange
        Mockito.when(platformRepository.getProductPlatformByNameIgnoreCase("origin")).thenReturn(testPlatform);
        // act
        boolean result = platformService.checkNameValidity("origin");
        // assert
        assertFalse(result);
    }

    @Test(expected = InvalidInputException.class)
    public void checkNameValidityBlankNameTest() throws Exception{
        // arrange
        // act
        platformService.checkNameValidity("");
        // assert
    }

    @Test
    public void checkNameValidityTest() throws Exception{
        // arrange
        Mockito.when(platformRepository.getProductPlatformByNameIgnoreCase("wow")).thenReturn(null);
        // act
        boolean result = platformService.checkNameValidity("wow");
        // assert
        assertTrue(result);
    }

    @Test
    public void getByPageTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<ProductPlatform> platformPage = new Page<ProductPlatform>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 3;
            }

            @Override
            public <U> Page<U> map(Function<? super ProductPlatform, ? extends U> converter) {
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
            public List<ProductPlatform> getContent() {
                return testPlatforms;
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
            public Iterator<ProductPlatform> iterator() {
                return null;
            }
        };
        Mockito.when(platformRepository.findAll(pageable)).thenReturn(platformPage);
        // act
        Page<ProductPlatform> platforms = platformService.getPlatforms(1, 5);
        // assert
        int sizeReturned = platforms.getContent().size();
        assertEquals(3, sizeReturned);
    }

    @Test(expected = InvalidInputException.class)
    public void getByPageNegativeSizeTest() throws Exception{
        // arrange
        // act
        platformService.getPlatforms(1, -1);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByPageNegativePageNrTest() throws Exception{
        // arrange
        // act
        platformService.getPlatforms(-1, 2);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByPageWithNoPlatformsTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<ProductPlatform> platformPage = new Page<ProductPlatform>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super ProductPlatform, ? extends U> converter) {
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
            public List<ProductPlatform> getContent() {
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
            public Iterator<ProductPlatform> iterator() {
                return null;
            }
        };
        Mockito.when(platformRepository.findAll(pageable)).thenReturn(platformPage);
        // act
        platformService.getPlatforms(1,5);
        // assert
    }

    @Test
    public void getByNamePageTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<ProductPlatform> platformPage = new Page<ProductPlatform>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 1;
            }

            @Override
            public <U> Page<U> map(Function<? super ProductPlatform, ? extends U> converter) {
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
            public List<ProductPlatform> getContent() {
                List<ProductPlatform> platforms = new ArrayList<>();
                platforms.add(testPlatform);
                return platforms;
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
            public Iterator<ProductPlatform> iterator() {
                return null;
            }
        };
        Mockito.when(platformRepository.findAllByNameIsContainingIgnoreCase("origin", pageable)).thenReturn(platformPage);
        // act
        Page<ProductPlatform> platforms = platformService.getPlatformsByName(1, 5, "origin");
        // assert
        int sizeReturned = platforms.getContent().size();
        assertEquals(1, sizeReturned);
    }

    @Test(expected = InvalidInputException.class)
    public void getByNamePageNegativeSizeTest() throws Exception{
        // arrange
        // act
        platformService.getPlatformsByName(1, -1, "origin");
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByNamePageNegativePageNrTest() throws Exception{
        // arrange
        // act
        platformService.getPlatformsByName(-1, 2, "origin");
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByNamePageNoGenresTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<ProductPlatform> platformPage = new Page<ProductPlatform>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super ProductPlatform, ? extends U> converter) {
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
            public List<ProductPlatform> getContent() {
                List<ProductPlatform> platforms = new ArrayList<>();
                return platforms;
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
            public Iterator<ProductPlatform> iterator() {
                return null;
            }
        };
        Mockito.when(platformRepository.findAllByNameIsContainingIgnoreCase("wow" ,pageable)).thenReturn(platformPage);
        // act
        platformService.getPlatformsByName(1,5, "wow");
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public  void getByNamePageInvalidNameTest() throws Exception{
        // arrange
        // act
        platformService.getPlatformsByName(1, 5, "");
        // assert
    }
}

