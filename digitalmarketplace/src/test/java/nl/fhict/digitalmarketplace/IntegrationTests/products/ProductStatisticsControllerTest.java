package nl.fhict.digitalmarketplace.IntegrationTests.products;

import nl.fhict.digitalmarketplace.IntegrationTests.AbstractTest;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.product.VideoGame;
import nl.fhict.digitalmarketplace.model.response.StatisticsItemResponse;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductStatisticsControllerTest extends AbstractTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductPlatformRepository platformRepository;
    @Autowired
    GenreRepository genreRepository;
    private Product testProduct;

    @Override
    @Before
    public void setUp(){
        super.setUp();
        String[] platformNames = {"origin","steam","battle.net", "ncsoft", "uplay", "xbox", "playstation", "android", "gog", "nintendo", "epic", "microsoft"};
        List<ProductPlatform> productPlatforms = new ArrayList<>();
        for (String name : platformNames){
            ProductPlatform platform = new ProductPlatform(name);
            productPlatforms.add(platform);
        }
        platformRepository.saveAll(productPlatforms);
        String[] genreNames = {"action", "shooter","survival","battle_royal","adventure","horror","rpg","racing", "sports","strategy","sandbox","open_world"};
        List<Genre> genres = new ArrayList<>();
        for (String name : genreNames){
            Genre genre = new Genre(name);
            genres.add(genre);
        }
        genreRepository.saveAll(genres);

        ProductPlatform testPlatform1 = new ProductPlatform(1, "origin");

        Genre testGenre = new Genre(1, "action");
        List<Genre> testGenres = new ArrayList<>();
        testGenres.add(testGenre);

        Date testDate = new Date();
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

        productRepository.save(product1);
        testProduct = product1;
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void countAllProducts() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/products/statistics/countAll";
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        StatisticsItemResponse[] responses = super.mapFromJson(content, StatisticsItemResponse[].class);
        assertNotNull(responses);
        assertEquals(1, responses.length);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void countProductsByPlatformsTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/products/statistics/countPlatforms";
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        StatisticsItemResponse[] responses = super.mapFromJson(content, StatisticsItemResponse[].class);
        assertNotNull(responses);
        assertEquals(12, responses.length);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void countProductsByGenresTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/products/statistics/countGenres";
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        StatisticsItemResponse[] responses = super.mapFromJson(content, StatisticsItemResponse[].class);
        assertNotNull(responses);
        assertEquals(12, responses.length);
    }
}
