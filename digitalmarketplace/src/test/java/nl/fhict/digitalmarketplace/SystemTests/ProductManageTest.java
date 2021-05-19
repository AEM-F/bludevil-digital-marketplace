package nl.fhict.digitalmarketplace.SystemTests;

import nl.fhict.digitalmarketplace.config.selenium.*;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.product.VideoGame;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductManageTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductPlatformRepository platformRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private BluDevilSeleniumLogin loginPage;
    private BluDevilAdminProductListControls adminProductListControls;
    private BluDevilSeleniumNavMenu navMenu;
    private BluDevilProductCreate productCreate;

    @Before
    public void setUp(){
        //Creating admin for tests
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName(ERole.ROLE_ADMIN);
        List<Role> admRoles = new ArrayList<>();
        admRoles.add(adminRole);
        User testAdmin = new User("testAdmin@gmail.com", passwordEncoder.encode("1234"));
        testAdmin.setRoles(admRoles);
        userRepository.save(testAdmin);

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

        productRepository.save(product1);
    }

    @Test
    public void inspectProductTest() throws Exception{
        // arrange
        String expectedUrl = "http://localhost:8080/#/products/1";
        loginPage = new BluDevilSeleniumLogin();
        adminProductListControls = new BluDevilAdminProductListControls(loginPage.getConfig());
        Thread.sleep(loginPage.getWaitTime());
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        // act
        adminProductListControls.inspectProduct(1);
        Thread.sleep(2000);
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        // assert
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void createProductTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        navMenu = new BluDevilSeleniumNavMenu(loginPage.getConfig());
        adminProductListControls = new BluDevilAdminProductListControls(loginPage.getConfig());
        productCreate = new BluDevilProductCreate(loginPage.getConfig());
        Thread.sleep(loginPage.getWaitTime());
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        navMenu.openNavMenu();
        Thread.sleep(1000);
        navMenu.navigateTo(ENavOption.PRODUCT_CREATION);
        Thread.sleep(1000);
        navMenu.closeNavMenu();
        Thread.sleep(1000);
        List<Integer> genres = new ArrayList<>();
        genres.add(1);
        genres.add(2);
        productCreate.enterProductData(
                "TestName",
                "12",
                "testDesc",
                "testSystemReq",
                "http://localhost:8080/api/images/getImage/picture-not-available.jpg",
                genres,
                "1999/05/04");
        Thread.sleep(1000);
        productCreate.submitCreateForm();
        Thread.sleep(2000);
        //assert
        boolean checkResult = adminProductListControls.checkIfProductExists(2);
        navMenu.closeBrowser();
        assertTrue(checkResult);
    }

    @Test
    public void deactivateProductTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        adminProductListControls = new BluDevilAdminProductListControls(loginPage.getConfig());
        Thread.sleep(loginPage.getWaitTime());
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //act
        adminProductListControls.deactivateProduct(1);
        Thread.sleep(1000);
        adminProductListControls.viewProductsByState(false);
        Thread.sleep(2000);
        //assert
        boolean checkResult = adminProductListControls.checkIfProductExists(1);
        adminProductListControls.closeBrowser();
        assertTrue(checkResult);
    }
}
