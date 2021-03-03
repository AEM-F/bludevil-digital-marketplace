package nl.fhict.digitalmarketplace;


import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductPlatformControllerTest extends AbstractTest{
    private ProductPlatform testPlatform;

    @Override
    @Before
    public void setUp(){
        super.setUp();
        ProductPlatform testPlatform = new ProductPlatform("riot");
        this.testPlatform = testPlatform;
    }

    @Test
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
