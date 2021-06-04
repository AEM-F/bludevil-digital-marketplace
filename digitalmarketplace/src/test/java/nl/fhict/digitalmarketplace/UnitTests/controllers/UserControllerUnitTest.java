package nl.fhict.digitalmarketplace.UnitTests.controllers;

import nl.fhict.digitalmarketplace.config.security.jwt.JwtUtils;
import nl.fhict.digitalmarketplace.controller.user.UserController;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.service.jwt.IRefreshTokenService;
import nl.fhict.digitalmarketplace.service.user.IUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerUnitTest {
    private Logger log = LoggerFactory.getLogger(UserControllerUnitTest.class);
    private User testUser;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private IUserService userService;
    @Mock
    private IRefreshTokenService refreshTokenService;
    @InjectMocks
    private UserController userController;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        // prepare test role
        List<Role> roleList = new ArrayList<>();
        Role testRole = new Role(ERole.ROLE_USER);
        testRole.setId(1);
        roleList.add(testRole);
        // prepare test user
        User testUser = new User();
        testUser.setId(1);
        testUser.setActive(true);
        testUser.setCreationDate(LocalDateTime.now());
        testUser.setEmail("test@gmail.com");
        testUser.setFirstName("Testy");
        testUser.setLastName("Tester");
        testUser.setPassword("shh...");
        testUser.setImagePath("");
        testUser.setRoles(roleList);
        this.testUser = testUser;
    }
    @After
    public void after(){
        log.info("[EndedTest]");
    }

    @Test
    public void getByIdTest() throws Exception{
        // arrange
        Mockito.when(userService.getById(1)).thenReturn(testUser);
        // act
        ResponseEntity<Object> responseEntity = userController.getUserById(1);
        // assert
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
