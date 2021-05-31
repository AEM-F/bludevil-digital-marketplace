package nl.fhict.digitalmarketplace.UnitTests.controllers;

import nl.fhict.digitalmarketplace.controller.product.ProductPlatformController;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.service.product.ProductPlatformService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PlatformControllerUnitTest {
    private ProductPlatform testPlatform;
    private List<ProductPlatform> testPlatforms;
    @Mock
    private ProductPlatformService productPlatformService;
    @InjectMocks
    private ProductPlatformController platformController;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.openMocks(this);
        this.testPlatform = new ProductPlatform(1, "origin");
        List<ProductPlatform> testPlatforms1 = new ArrayList<>();
        testPlatforms1.add(testPlatform);
        testPlatforms1.add(new ProductPlatform(2, "steam"));
        testPlatforms1.add(new ProductPlatform(3, "uplay"));
        this.testPlatforms = testPlatforms1;
        Mockito.when(productPlatformService.getPlatformById(1)).thenReturn(testPlatform);
        Mockito.when(productPlatformService.getAllPlatforms()).thenReturn(testPlatforms1);
        Mockito.when(productPlatformService.createProductPlatform(testPlatform)).thenReturn(testPlatform);
        Mockito.when(productPlatformService.getPlatformByName("origin")).thenReturn(testPlatform);
        Mockito.when(productPlatformService.updatePlatform(testPlatform, 1)).thenReturn(testPlatform);
        Mockito.when(productPlatformService.checkNameValidity("origin")).thenReturn(true);
    }

    @Test
    public void getPlatformByIdTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = platformController.getPlatformById(1);
        HttpStatus responseEntityStatusCode = responseEntity.getStatusCode();
        int status = responseEntityStatusCode.value();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void createProductPlatformTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = platformController.createPlatform(this.testPlatform);
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void updateProductPlatformTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = platformController.updatePlatform(this.testPlatform, 1);
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void getAllPlatformsTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = platformController.getPlatforms(1, 5, "all");
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }

    @Test
    public void checkNameValidityTest() throws Exception{
        //arrange
        //act
        ResponseEntity<Object> responseEntity = platformController.checkPlatformNameValidity("origin");
        int status = responseEntity.getStatusCodeValue();
        //assert
        assertEquals(200, status);
    }
}
