package nl.fhict.digitalmarketplace.IntegrationTests.user;

import nl.fhict.digitalmarketplace.IntegrationTests.AbstractTest;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import nl.fhict.digitalmarketplace.service.user.AdminAccountGenerator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountGeneratorTest extends AbstractTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminAccountGenerator adminAccountGenerator;
    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void creationWithNoAdminAccountTest() throws Exception{
        // arrange
        // act
        User admin = userRepository.findByEmail("admin_email@gmail.com");
        // assert
        assertNotNull(admin);
    }
}
