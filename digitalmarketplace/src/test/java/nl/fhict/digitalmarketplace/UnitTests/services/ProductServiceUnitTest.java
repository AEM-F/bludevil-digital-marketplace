package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.*;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import nl.fhict.digitalmarketplace.service.product.ProductService;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceUnitTest {
    private Logger log = LoggerFactory.getLogger(ProductServiceUnitTest.class);
    private Product testProduct;
    private List<Product> testProducts;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductPlatformRepository productPlatformRepository;
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private ProductService productService;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.openMocks(this);
        List<Product> testProducts = new ArrayList<>();
        ProductPlatform testPlatform1 = new ProductPlatform(1, "origin");

        Genre testGenre = new Genre(1, "action");
        List<Genre> testGenres = new ArrayList<>();
        testGenres.add(testGenre);
        Date testLocalDate = new Date();
//        LocalDate testLocalDate = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//        String testDate = formatter.format(testLocalDate);

        VideoGame product1 = new VideoGame();
        product1.setName("Antem");
        product1.setProductPlatform(testPlatform1);
        product1.setQuantity(0);
        product1.setPrice(39.99);
        product1.setDescription("Cool game");
        product1.setSystemRequirements("On windows");
        product1.setActive(true);
        product1.setReleaseDate(testLocalDate);
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
    public void validateProductPlatformTest() throws Exception{
        // arrange
        Mockito.when(productPlatformRepository.getById(1)).thenReturn(((VideoGame)testProduct).getProductPlatform());
        // act
        boolean result = productService.validateProductPlatform(testProduct);
        // assert
        assertTrue(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void validateProductPlatformWithNotExistingPlatformTest() throws Exception{
        // arrange
        Mockito.when(productPlatformRepository.getById(1)).thenReturn(null);
        // act
        boolean result = productService.validateProductPlatform(testProduct);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void validateProductPlatformWithDifferentPlatformThenProvidedNameTest() throws Exception{
        // arrange
        ProductPlatform platform = new ProductPlatform("sandbox");
        Mockito.when(productPlatformRepository.getById(1)).thenReturn(platform);
        // act
        boolean result = productService.validateProductPlatform(testProduct);
        // assert
    }

    @Test
    public void validateGenresTest() throws Exception{
        // arrange
        Mockito.when(genreRepository.findAll()).thenReturn(((VideoGame)testProduct).getGenres());
        // act
        boolean result = productService.validateProductGenres(testProduct);
        // assert
        assertTrue(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void validateGenresWithNoGenresTest() throws Exception{
        // arrange
        List<Genre> genres = new ArrayList<>();
        Mockito.when(genreRepository.findAll()).thenReturn(genres);
        // act
        boolean result = productService.validateProductGenres(testProduct);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void validateGenreWithInvalidGenresTest() throws Exception{
        // arrange
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("test"));
        Mockito.when(genreRepository.findAll()).thenReturn(genres);
        // act
        boolean result = productService.validateProductGenres(testProduct);
        // assert
    }

    @Test
    public void createProductTest() throws Exception{
        // arrange
        Mockito.when(productPlatformRepository.getById(1)).thenReturn(testProduct.getProductPlatform());
        Mockito.when(genreRepository.findAll()).thenReturn(((VideoGame)testProduct).getGenres());
        Mockito.when(productRepository.save(testProduct)).thenReturn(testProduct);
        // act
        Product returnedProduct = productService.createProduct(testProduct);
        // assert
        String productName = returnedProduct.getName();
        boolean result = productName.equals(testProduct.getName());
        assertTrue(result);
    }

    @Test
    public void getByIdTest() throws Exception{
        // arrange
        Mockito.when(productRepository.getById(1)).thenReturn(testProduct);
        // act
        Product returnedProduct = productService.getProductById(1);
        // assert
        String productName = returnedProduct.getName();
        boolean result = productName.equals(testProduct.getName());
        assertTrue(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByIdNotFoundTest() throws Exception{
        // arrange
        Mockito.when(productRepository.getById(1)).thenReturn(null);
        // act
        Product product = productService.getProductById(1);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByIdNegativeIdTest() throws Exception{
        // arrange
        // act
        Product product = productService.getProductById(-1);
        // assert
    }

    @Test
    public void updateProductTest() throws Exception{
        // arrange
        VideoGame insertedProduct = new VideoGame();
        insertedProduct.setGenres(((VideoGame)testProduct).getGenres());
        insertedProduct.setProductPlatform(testProduct.getProductPlatform());
        insertedProduct.setName("Anthem Deluxe");
        Mockito.when(productPlatformRepository.getById(1)).thenReturn(insertedProduct.getProductPlatform());
        Mockito.when(productRepository.getById(1)).thenReturn(testProduct);
        Mockito.when(genreRepository.findAll()).thenReturn(insertedProduct.getGenres());
        Mockito.when(productRepository.save(insertedProduct)).thenReturn(insertedProduct);
        // act
        Product returnedProduct = productService.updateProduct(insertedProduct, 1);
        // assert
        String productName = returnedProduct.getName();
        boolean result = productName.equals(insertedProduct.getName());
        assertTrue(result);
    }

    @Test(expected = InvalidInputException.class)
    public void updateProductNegativeIdTest() throws Exception{
        // arrange
        // act
        Product product = productService.updateProduct(testProduct, -1);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateProductWithNonExistingIdTest() throws Exception{
        // arrange
        Mockito.when(productRepository.getById(22)).thenReturn(null);
        // act
        Product product = productService.updateProduct(testProduct, 22);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void updateProductWithDifferentProductTypeTest() throws Exception{
        // arrange
        Product productToUpdate = new SoftwareProduct();
        productToUpdate.setName("Valheim");
        productToUpdate.setProductPlatform(testProduct.getProductPlatform());
        productToUpdate.setQuantity(0);
        productToUpdate.setPrice(39.99);
        productToUpdate.setDescription("Cool game");
        productToUpdate.setSystemRequirements("On windows");
        productToUpdate.setActive(true);
        Mockito.when(productRepository.getById(1)).thenReturn(testProduct);
        // act
        Product product = productService.updateProduct(productToUpdate, 1);
        // assert
    }

    @Test
    public void deleteProductByIdTest() throws Exception{
        // arrange
        Mockito.when(productRepository.getById(1)).thenReturn(testProduct);
        // act
        Product product = productService.deleteProductById(1);
        // assert
        boolean productStatus = product.getActive();
        assertFalse(productStatus);
    }

    @Test(expected = InvalidInputException.class)
    public void deleteProductByIdWithNegativeIdTest() throws Exception{
        // arrange
        // act
        Product product = productService.deleteProductById(-1);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteProductByIdWithNonExistingProductTest() throws Exception{
        // arrange
        Mockito.when(productRepository.getById(22)).thenReturn(null);
        // act
        Product product = productService.deleteProductById(22);
        // assert
    }

    @Test
    public void getByPageTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productPage = new Page<Product>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 3;
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
        Mockito.when(productRepository.findAllByIsActive(true ,pageable)).thenReturn(productPage);
        // act
        Page<Product> products = productService.getProducts(1, 5, true);
        // assert
        int sizeReturned = products.getContent().size();
        assertEquals(1, sizeReturned);
    }

    @Test(expected = InvalidInputException.class)
    public void getByPageNegativeSizeTest() throws Exception{
        // arrange
        // act
        productService.getProducts(1, -1, true);
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByPageNegativePageNrTest() throws Exception{
        // arrange
        // act
        productService.getProducts(-1, 2, true);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByPageNoProductsTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
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
            public Iterator<Product> iterator() {
                return null;
            }
        };
        Mockito.when(productRepository.findAllByIsActive(true ,pageable)).thenReturn(productPage);
        // act
        productService.getProducts(1,5, true);
        // assert
    }

    @Test
    public void getByNamePageTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
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
                List<Product> products = new ArrayList<>();
                products.add(testProduct);
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
        Mockito.when(productRepository.findAllByNameIsContainingIgnoreCase("Antem", pageable)).thenReturn(productPage);
        // act
        Page<Product> products = productService.getProductsByName(1, 5, "Antem");
        // assert
        int sizeReturned = products.getContent().size();
        assertEquals(1, sizeReturned);
    }

    @Test(expected = InvalidInputException.class)
    public void getByNamePageNegativeSizeTest() throws Exception{
        // arrange
        // act
        productService.getProductsByName(1, -1, "Antem");
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public void getByNamePageNegativePageNrTest() throws Exception{
        // arrange
        // act
        productService.getProductsByName(-1, 2, "Antem");
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByNamePageNoProductsTest() throws Exception{
        // arrange
        Pageable pageable = PageRequest.of(0, 5);
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
        Mockito.when(productRepository.findAllByNameIsContainingIgnoreCase("Antem" ,pageable)).thenReturn(productPage);
        // act
        productService.getProductsByName(1,5, "Antem");
        // assert
    }

    @Test(expected = InvalidInputException.class)
    public  void getByNamePageInvalidNameTest() throws Exception{
        // arrange
        // act
        productService.getProductsByName(1, 5, "");
        // assert
    }
}
