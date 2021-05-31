package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import nl.fhict.digitalmarketplace.service.user.UserDetailsService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDetailsServiceUnitTest {
    private Logger log = LoggerFactory.getLogger(UserDetailsServiceUnitTest.class);
    private User testUser;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsService userDetailsService;

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
    public void loadUserByUsernameTest(){
        // arrange
        Mockito.when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);
        // act
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getEmail());
        // assert
        assertNotNull(userDetails);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameWithNotExistingUserTest(){
        // arrange
        Mockito.when(userRepository.findByEmail(testUser.getEmail())).thenReturn(null);
        // act
        userDetailsService.loadUserByUsername(testUser.getEmail());
        // assert
    }
}
