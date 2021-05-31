package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.user.RoleRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import nl.fhict.digitalmarketplace.service.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceUnitTest {
    private Logger log = LoggerFactory.getLogger(UserServiceUnitTest.class);
    private User testUser;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserService userService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        List<Role> roleList = new ArrayList<>();
        Role testRole = new Role(ERole.ROLE_USER);
        testRole.setId(1);
        roleList.add(testRole);
        // prepare test user
        User testUser = new User();
        testUser.setId(1);
        testUser.setActive(true);
        testUser.setCreationDate(LocalDate.now());
        testUser.setEmail("test@gmail.com");
        testUser.setFirstName("Testy");
        testUser.setLastName("Tester");
        testUser.setPassword("shh...");
        testUser.setImagePath("");
        testUser.setRoles(roleList);
        this.testUser = testUser;
        log.info("[StartedTest]");
    }
    @After
    public void after(){
        log.info("[EndedTest]");
    }

    @Test
    public void createUserTest() throws Exception{
        // arrange
        Mockito.when(userRepository.findByEmail(testUser.getEmail())).thenReturn(null);
        // act
        User createdUser = userService.createUser(testUser);
        // assert
        assertNotNull(createdUser);
    }

    @Test(expected = InvalidInputException.class)
    public void createUserWithInvalidEmailTest() throws Exception{
        // arrange
        Mockito.when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);
        // act
        userService.createUser(testUser);
        // assert
    }

    @Test
    public void getByIdTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(testUser);
        // act
        User user = userService.getById(1);
        // assert
        assertNotNull(user);
    }

    @Test(expected = InvalidInputException.class)
    public void getByIdWithNegativeNrTest() throws Exception{
        // arrange
        // act
        userService.getById(-1);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByIdWithNonExistingUserTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(null);
        // act
        userService.getById(1);
        // assert
    }

    @Test
    public void updateUserWithTheSameEmailTest() throws Exception{
        // arrange
        User toUpdateUser = new User();
        toUpdateUser.setFirstName("Lucky");
        Mockito.when(userRepository.getById(1)).thenReturn(testUser);
        Mockito.when(userRepository.save(toUpdateUser)).thenReturn(toUpdateUser);
        // act
        User updatedUser = userService.updateUser(toUpdateUser, 1);
        // assert
        assertEquals(toUpdateUser.getFirstName(), updatedUser.getFirstName());
    }

    @Test
    public void updateUserWithDifferentEmailTest() throws Exception{
        // arrange
        User toUpdateUser = new User();
        toUpdateUser.setFirstName("Lucky");
        toUpdateUser.setEmail("wowowo@gmail.com");
        Mockito.when(userRepository.getById(1)).thenReturn(testUser);
        Mockito.when(userRepository.save(toUpdateUser)).thenReturn(toUpdateUser);
        Mockito.when(userRepository.findByEmail("wowowo@gmail.com")).thenReturn(null);
        // act
        User updatedUser = userService.updateUser(toUpdateUser, 1);
        // assert
        assertEquals(toUpdateUser.getFirstName(), updatedUser.getFirstName());
    }

    @Test(expected = InvalidInputException.class)
    public void updateUserWithExistingEmailTest() throws Exception{
        // arrange
        User toUpdateUser = new User();
        toUpdateUser.setFirstName("Lucky");
        toUpdateUser.setEmail("wowowo@gmail.com");
        Mockito.when(userRepository.getById(1)).thenReturn(testUser);
        Mockito.when(userRepository.findByEmail("wowowo@gmail.com")).thenReturn(testUser);
        Mockito.when(userRepository.save(toUpdateUser)).thenReturn(toUpdateUser);
        // act
         userService.updateUser(toUpdateUser, 1);
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateUserWithNotExistingUserTest() throws Exception{
        // arrange
        User toUpdateUser = new User();
        toUpdateUser.setFirstName("Lucky");
        toUpdateUser.setEmail("wowowo@gmail.com");
        Mockito.when(userRepository.getById(1)).thenReturn(null);
        // act
        userService.updateUser(toUpdateUser, 1);
        // assert
    }
}
