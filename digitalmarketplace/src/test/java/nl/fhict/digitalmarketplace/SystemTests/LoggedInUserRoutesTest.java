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
public class LoggedInUserRoutesTest {
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
    public void accessSupportChatAsGuestTest() throws Exception{
        //arrange
        String supportChatUrl = "http://localhost:8080/#/supportChat";
        String expectedUrl = "http://localhost:8080/#/login?returnUrl=%2FsupportChat";
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(supportChatUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessSupportChatAsMemberTest() throws Exception{
        //arrange
        String supportUrl = "http://localhost:8080/#/supportChat";
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        loginPage.typeUserCredentialsLogin("test@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(supportUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(supportUrl, returnedUrl);
    }

    @Test
    public void accessSupportChatAsAdminTest() throws Exception{
        //arrange
        String supportUrl = "http://localhost:8080/#/supportChat";
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(supportUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(supportUrl, returnedUrl);
    }

    @Test
    public void accessUserProfileAsGuestTest() throws Exception{
        //arrange
        String userProfileUrl = "http://localhost:8080/#/account";
        String expectedUrl = "http://localhost:8080/#/login?returnUrl=%2Faccount";
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(userProfileUrl);
        Thread.sleep(3000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void accessUserProfileAsMemberTest() throws Exception{
        //arrange
        String accountUrl = "http://localhost:8080/#/account";
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        loginPage.typeUserCredentialsLogin("test@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(accountUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(accountUrl, returnedUrl);
    }

    @Test
    public void accessUserProfileAsAdminTest() throws Exception{
        //arrange
        String accountUrl = "http://localhost:8080/#/account";
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        loginPage.getConfig().getDriver().navigate().to(accountUrl);
        Thread.sleep(1000);
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(accountUrl, returnedUrl);
    }
}
