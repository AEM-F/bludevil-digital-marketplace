package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.product.VideoGame;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import nl.fhict.digitalmarketplace.service.product.filter.*;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductFilterServiceUnitTest {
    private Logger log = LoggerFactory.getLogger(ProductFilterServiceUnitTest.class);
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductFilterService productFilterService;
    private Product testProduct;
    private List<Product> testProducts = new ArrayList<>();

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        List<Product> testProducts = new ArrayList<>();
        ProductPlatform testPlatform1 = new ProductPlatform(1, "origin");

        Genre testGenre = new Genre(1, "action");
        List<Genre> testGenres = new ArrayList<>();
        testGenres.add(testGenre);

        LocalDate testLocalDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String testDate = formatter.format(testLocalDate);

        VideoGame product1 = new VideoGame();
        product1.setName("Antem");
        product1.setProductPlatform(testPlatform1);
        product1.setQuantity(0);
        product1.setPrice(39.99);
        product1.setDescription("Cool game");
        product1.setSystemRequirements("On windows");
        product1.setActive(true);
        product1.setReleaseDate(testDate);
        product1.setGenres(testGenres);
        this.testProduct = product1;
        testProducts.add(product1);
        this.testProducts = testProducts;
        log.info("[StartedTest]");
    }

    @After
    public void after(){
        log.info("[EndedTest]");
    }

    @Test
    public void filterByPlatformTest() throws Exception{
        // arrange
        ProductFilterSpec testSpec = new ProductPlatformFilterSpec("origin");
        Pageable requestedPage = PageRequest.of(0, 5);
        Page<Product> productPage = new Page<Product>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 1;
            }

            @Override
            public <U> Page<U> map(Function<? super Product, ? extends U> converter) {
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
            public List<Product> getContent() {
                return testProducts;
            }

            @Override
            public boolean hasContent() {
                return true;
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
            public Iterator<Product> iterator() {
                return null;
            }
        };
        Mockito.when(productRepository.findAllByIsActiveAndProductPlatform_Name(true, "origin",requestedPage)).thenReturn(productPage);
        // act
        Page<Product> returnedPage = productFilterService.filterBy(1, 5, testSpec, true);
        // assert
        int pageSize = returnedPage.getSize();
        assertEquals(1, pageSize);
    }

    @Test
    public void filterByPriceTest() throws Exception{
        // arrange
        ProductFilterSpec testSpec = new ProductPriceFilterSpec(39.99);
        Pageable requestedPage = PageRequest.of(0, 5);
        Page<Product> productPage = new Page<Product>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 1;
            }

            @Override
            public <U> Page<U> map(Function<? super Product, ? extends U> converter) {
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
            public List<Product> getContent() {
                return testProducts;
            }

            @Override
            public boolean hasContent() {
                return true;
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
            public Iterator<Product> iterator() {
                return null;
            }
        };
        Mockito.when(productRepository.findAllByIsActiveAndPrice(true, 39.99,requestedPage)).thenReturn(productPage);
        // act
        Page<Product> returnedPage = productFilterService.filterBy(1, 5, testSpec, true);
        // assert
        int pageSize = returnedPage.getSize();
        assertEquals(1, pageSize);
    }

    @Test
    public void filterByPriceAndPlatformTest() throws Exception{
        // arrange
        ProductFilterSpec testSpec = new ProductPriceAndPlatformFilterSpec(39.99, "origin");
        Pageable requestedPage = PageRequest.of(0, 5);
        Page<Product> productPage = new Page<Product>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 1;
            }

            @Override
            public <U> Page<U> map(Function<? super Product, ? extends U> converter) {
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
            public List<Product> getContent() {
                return testProducts;
            }

            @Override
            public boolean hasContent() {
                return true;
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
            public Iterator<Product> iterator() {
                return null;
            }
        };
        Mockito.when(productRepository.findAllByIsActiveAndProductPlatform_NameAndPrice(true, "origin",39.99,requestedPage)).thenReturn(productPage);
        // act
        Page<Product> returnedPage = productFilterService.filterBy(1, 5, testSpec, true);
        // assert
        int pageSize = returnedPage.getSize();
        assertEquals(1, pageSize);
    }

    @Test(expected = InvalidInputException.class)
    public void filterByNegativePageNrTest() throws Exception{
        // arrange
        // act
        productFilterService.filterBy(-1, 1, new ProductPriceFilterSpec(22), true);
        // arrange
    }

    @Test(expected = InvalidInputException.class)
    public void filterByNegativeSizeNrTest() throws Exception{
        // arrange
        // act
        productFilterService.filterBy(1, -1, new ProductPriceFilterSpec(22), true);
        // arrange
    }

    @Test(expected = ResourceNotFoundException.class)
    public void filterByWithNoProductTest() throws Exception{
        // arrange
        ProductFilterSpec testSpec = new ProductPlatformFilterSpec("origin");
        Pageable requestedPage = PageRequest.of(0, 5);
        Page<Product> productPage = new Page<Product>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super Product, ? extends U> converter) {
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
            public List<Product> getContent() {
                List<Product> products = new ArrayList<>();
                return products;
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
            public Iterator<Product> iterator() {
                return null;
            }
        };
        Mockito.when(productRepository.findAllByIsActiveAndProductPlatform_Name(true, "origin",requestedPage)).thenReturn(productPage);
        // act
        Page<Product> returnedPage = productFilterService.filterBy(1, 5, testSpec, true);
        // assert
    }
}
