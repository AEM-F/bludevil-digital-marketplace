package nl.fhict.digitalmarketplace.SystemTests;

import nl.fhict.digitalmarketplace.DigitalmarketplaceApplication;
import nl.fhict.digitalmarketplace.config.selenium.BluDevilSeleniumLogin;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
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
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LoginPageTest {
    @Autowired
    DigitalmarketplaceApplication digitalmarketplaceApplication;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private BluDevilSeleniumLogin loginPage;

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

        //Creating inactive user for tests
        User testUserInactive = new User("test2@gmail.com", passwordEncoder.encode("1234"));
        testUserInactive.setRoles(roles);
        testUserInactive.setActive(false);
        userRepository.save(testUserInactive);
    }

    @Test
    public void loginMemberTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String expectedUrl = "http://localhost:8080/#/account";
        //act
        loginPage.typeUserCredentialsLogin("test@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void loginAdminTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String expectedUrl = "http://localhost:8080/#/admin/dashboard";
        //act
        loginPage.typeUserCredentialsLogin("testAdmin@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void loginWrongCredentialsTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String expectedUrl = "http://localhost:8080/#/login";
        //act
        loginPage.typeUserCredentialsLogin("testWrong@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void loginInactiveUserTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String expectedUrl = "http://localhost:8080/#/login";
        //act
        loginPage.typeUserCredentialsLogin("test2@gmail.com", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitLoginForm();
        Thread.sleep(loginPage.getWaitTime());
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }

    @Test
    public void signupUserTest() throws Exception{
        //arrange
        loginPage = new BluDevilSeleniumLogin();
        Thread.sleep(loginPage.getWaitTime());
        String expectedUrl = "http://localhost:8080/#/account";
        //act
        loginPage.navigateToSignup();
        Thread.sleep(loginPage.getWaitTime());
        loginPage.typeUserCredentialsSignup("testSignup@gmail.com", "1234", "1234");
        Thread.sleep(loginPage.getWaitTime());
        loginPage.submitSignupForm();
        Thread.sleep(loginPage.getWaitTime());
        //assert
        String returnedUrl = loginPage.getCurrentUrl();
        loginPage.closeBrowser();
        assertEquals(expectedUrl, returnedUrl);
    }
}
