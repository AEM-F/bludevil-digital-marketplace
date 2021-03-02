package nl.fhict.digitalmarketplace;

import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.product.VideoGame;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTest extends AbstractTest{

    @Autowired
    ProductRepository productRepository;
    private Product testProduct;

    @Override
    @Before
    public void setUp(){
        super.setUp();
        ProductPlatform testPlatform1 = new ProductPlatform(1, "origin");
        Genre testGenre = new Genre(1, "action");
        List<Genre> genres = new ArrayList<>();
        genres.add(testGenre);
        LocalDate testLocalDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String testDate = formatter.format(testLocalDate);
        Product product1 = new VideoGame("Antem", testPlatform1, 0, 39.99, "Cool game", "smh/inapp", "On Windows", true, testDate, genres );
        //Product firstProduct = new Product("Anthem", testPlatform1, 0, 39.99, "Cool game", "somewhere/inMyApp", true);
        productRepository.save(product1);
        testProduct = product1;
    }

    @Test
    public void getProductByIdTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/products/1";
        Product expectedProduct = testProduct;
        expectedProduct.setId(1);
        String testProductJson = super.mapToJson(expectedProduct);
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Product product = super.mapFromJson(content, VideoGame.class);
        assertNotNull(product);
        assertEquals(testProductJson, content);
    }

    @Test
    public void addProductTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/products/";
        ProductPlatform productPlatform = new ProductPlatform(2, "steam");
        Product productToMatch = new VideoGame(2,"Valheim",productPlatform,0,16.00,"new game", "i/am/somewhere","On Pc", true, ((VideoGame)testProduct).getReleaseDate(), ((VideoGame)testProduct).getGenres());
        String toMatchJson = super.mapToJson(productToMatch);
        Product productToInsert = new VideoGame("Valheim",productPlatform,0,16.00,"new game", "i/am/somewhere","On Pc", true, ((VideoGame)testProduct).getReleaseDate(), ((VideoGame)testProduct).getGenres() );
        String inputJson = super.mapToJson(productToInsert);
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(toMatchJson, content);
    }

    @Test
    public void deleteProductTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/products/1";
        Product expectedProduct = testProduct;
        expectedProduct.setId(1);
        String testProductJson = super.mapToJson(expectedProduct);
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(testProductJson, content);
    }

    @Test
    public void updateProductTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/products/1";
        Product productToUpdate = new VideoGame("The Forest",testProduct.getProductPlatform(),0,16.00,"new game", "i/am/somewhere","On Pc", true, ((VideoGame)testProduct).getReleaseDate(), ((VideoGame)testProduct).getGenres() );
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(super.mapToJson(productToUpdate))).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        productToUpdate.setId(1);
        String expected = super.mapToJson(productToUpdate);
        assertEquals(expected,content);
    }
}
