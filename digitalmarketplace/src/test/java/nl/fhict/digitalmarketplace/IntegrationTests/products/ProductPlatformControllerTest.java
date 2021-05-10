package nl.fhict.digitalmarketplace.IntegrationTests.products;



import nl.fhict.digitalmarketplace.IntegrationTests.AbstractTest;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductPlatformControllerTest extends AbstractTest {

    private ProductPlatform testPlatform;
    @Autowired
    ProductPlatformRepository platformRepository;
    @Autowired
    GenreRepository genreRepository;

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

        this.testPlatform = new ProductPlatform("riot");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getProductPlatformByIdTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/productPlatforms/1";
        ProductPlatform expectedPlatform = new ProductPlatform(1, "origin");
        String expectedPlatformJson = super.mapToJson(expectedPlatform);
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedPlatformJson, content);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void addProductPlatformTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/productPlatforms/";
        ProductPlatform platformToMatch = new ProductPlatform(13, testPlatform.getName());
        ProductPlatform inputPlatform = testPlatform;
        String platformToMatchJson = super.mapToJson(platformToMatch);
        String inputPlatformJson = super.mapToJson(inputPlatform);
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputPlatformJson)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(platformToMatchJson, content);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void updateProductPlatformTest() throws Exception{
        //arrange
        String uri = "http://localhost:8080/api/productPlatforms/1";
        ProductPlatform platformToUpdate = testPlatform;
        String platformToUpdateJson = super.mapToJson(platformToUpdate);
        //act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(platformToUpdateJson)).andReturn();
        //assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        platformToUpdate.setId(1);
        String expected = super.mapToJson(platformToUpdate);
        assertEquals(expected,content);
    }
}
