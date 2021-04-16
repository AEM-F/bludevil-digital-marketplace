package nl.fhict.digitalmarketplace.UnitTests.controllers;

import nl.fhict.digitalmarketplace.controller.product.ProductController;
import nl.fhict.digitalmarketplace.model.product.Genre;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerUnitTest2 {
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
        Mockito.when(productService.getProductById(1)).thenReturn(product1);
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
}
