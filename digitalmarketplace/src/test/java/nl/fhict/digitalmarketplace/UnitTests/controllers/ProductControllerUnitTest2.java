package nl.fhict.digitalmarketplace.UnitTests.controllers;

import nl.fhict.digitalmarketplace.controller.product.ProductController;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.product.VideoGame;
import nl.fhict.digitalmarketplace.service.product.ProductPlatformService;
import nl.fhict.digitalmarketplace.service.product.ProductService;
import nl.fhict.digitalmarketplace.service.product.filter.ProductFilterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class ProductControllerUnitTest2 {
    private Product testProduct;
    @Mock
    ProductService productService;

    @Mock
    ProductFilterService productFilterService;

    @Mock
    ProductPlatformService productPlatformService;

    @InjectMocks // inject mock into the product controller
    ProductController productController;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.openMocks(this);

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
        Mockito.when(productService.getProductById(1)).thenReturn(product1);
        List<Product> products = new ArrayList<>();
        products.add(product1);
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
                return products;
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
        Mockito.when(productService.getProducts(1,1, true)).thenReturn(productPage);
        Mockito.when(productService.createProduct(product1)).thenReturn(product1);
        Mockito.when(productService.deleteProductById(1)).thenReturn(product1);
        Mockito.when(productService.updateProduct(product1, 1)).thenReturn(product1);
    }

    @Test
    public void getProductByIdTest() throws Exception{
        //arrange

        //act
        ResponseEntity<Object> responseEntity = productController.getProduct(1);
        HttpStatus responseEntityStatusCode = responseEntity.getStatusCode();
        int status = responseEntityStatusCode.value();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void getProductsByPageAndSizeTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = productController.getProducts(1,1,null, true);
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void createProductTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = productController.createProduct(this.testProduct);
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void updateProductTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = productController.updateProduct(this.testProduct, 1);
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void deactivateProductTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = productController.deleteProduct(1);
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }
}
