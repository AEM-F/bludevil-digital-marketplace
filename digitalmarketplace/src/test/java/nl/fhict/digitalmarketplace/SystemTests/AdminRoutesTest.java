package nl.fhict.digitalmarketplace.SystemTests;

import nl.fhict.digitalmarketplace.DigitalmarketplaceApplication;
import nl.fhict.digitalmarketplace.config.selenium.BluDevilAdminProductListControls;
import nl.fhict.digitalmarketplace.config.selenium.BluDevilProductCreate;
import nl.fhict.digitalmarketplace.config.selenium.BluDevilSeleniumLogin;
import nl.fhict.digitalmarketplace.config.selenium.BluDevilSeleniumNavMenu;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.product.VideoGame;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminRoutesTest {
    @Autowired
    DigitalmarketplaceApplication digitalmarketplaceApplication;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ProductRepository productRepository;
    private BluDevilSeleniumLogin loginPage;
    private BluDevilAdminProductListControls adminProductListControls;
    private BluDevilSeleniumNavMenu navMenu;
    private BluDevilProductCreate productCreate;

    @Before
    public void setUp(){
        digitalmarketplaceApplication.insertDataTest();
        //Creating member for tests
        Role memberRole = new Role();
        memberRole.setId(1);
        memberRole.setName(ERole.ROLE_USER);
        List<Role> roles = new ArrayList<>();
        roles.add(memberRole);
        User testUser = new User("test@gmail.com", passwordEncoder.encode("1234"));
        testUser.setRoles(roles);
        userRepository.save(testUser);

        //Creating admin for tests
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName(ERole.ROLE_ADMIN);
        List<Role> admRoles = new ArrayList<>();
        admRoles.add(adminRole);
        User testAdmin = new User("testAdmin@gmail.com", passwordEncoder.encode("1234"));
        testAdmin.setRoles(admRoles);
        userRepository.save(testAdmin);

        ProductPlatform testPlatform1 = new ProductPlatform(1, "origin");

        Genre testGenre = new Genre(1, "action");
        List<Genre> testGenres = new ArrayList<>();
        testGenres.add(testGenre);

        Date testLocalDate = new Date();
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

        productRepository.save(product1);
    }

    @Test
    public void accessAdminDashboardAsGuestTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminDashboardUrl = "http://localhost:8080/#/admin/dashboard";
        String expectedUrl = "http://localhost:8080/#/login?returnUrl=%2Fadmin%2Fdashboard";
        //act
        loginPage.getConfig().getDriver().navigate().to(adminDashboardUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessAdminDashboardAsMemberTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminDashboardUrl = "http://localhost:8080/#/admin/dashboard";
        String expectedUrl = "http://localhost:8080/#/?returnUrl=%2Fadmin%2Fdashboard";
        loginPage.typeUserCredentialsLogin("test@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(adminDashboardUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessAdminDashboardAsAdminTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminDashboardUrl = "http://localhost:8080/#/admin/dashboard";
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(adminDashboardUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(adminDashboardUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductManageAsGuestTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products";
        String expectedUrl = "http://localhost:8080/#/login?returnUrl=%2Fadmin%2Fproducts";
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductManageAsMemberTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products";
        String expectedUrl = "http://localhost:8080/#/?returnUrl=%2Fadmin%2Fproducts";
        loginPage.typeUserCredentialsLogin("test@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductManageAsAdminTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products";
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(adminProductsUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductCreateAsGuestTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products/create";
        String expectedUrl = "http://localhost:8080/#/login?returnUrl=%2Fadmin%2Fproducts%2Fcreate";
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductCreateAsMemberTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products/create";
        String expectedUrl = "http://localhost:8080/#/?returnUrl=%2Fadmin%2Fproducts%2Fcreate";
        loginPage.typeUserCredentialsLogin("test@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductCreateAsAdminTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products/create";
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(adminProductsUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductEditAsGuestTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products/edit/1";
        String expectedUrl = "http://localhost:8080/#/login?returnUrl=%2Fadmin%2Fproducts%2Fedit%2F1";
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductEditAsMemberTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products/edit/1";
        String expectedUrl = "http://localhost:8080/#/?returnUrl=%2Fadmin%2Fproducts%2Fedit%2F1";
        loginPage.typeUserCredentialsLogin("test@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessAdminProductEditAsAdminTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String adminProductsUrl = "http://localhost:8080/#/admin/products/edit/1";
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(adminProductsUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(adminProductsUrl, returnedUrl);
    }
}
