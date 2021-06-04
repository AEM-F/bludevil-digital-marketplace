package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.InvalidUUIDPatternException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.customException.TokenRefreshException;
import nl.fhict.digitalmarketplace.model.jwt.RefreshToken;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.jwt.RefreshTokenRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import nl.fhict.digitalmarketplace.service.jwt.RefreshTokenService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RefreshTokenServiceUnitTest {
    private Logger log = LoggerFactory.getLogger(RefreshTokenServiceUnitTest.class);
    private RefreshToken testToken1;
    private RefreshToken testToken2;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 200000L);
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
        // prepare test token valid exp
        RefreshToken testRefreshToken = new RefreshToken();
        testRefreshToken.setId(1);
        testRefreshToken.setUser(testUser);
        testRefreshToken.setExpiryDate(Instant.now().plusMillis(200000));
        testRefreshToken.setToken(UUID.randomUUID().toString());
        this.testToken1 = testRefreshToken;
        // prepare test token invalid exp
        RefreshToken testRefreshToken2 = new RefreshToken();
        testRefreshToken2.setId(2);
        testRefreshToken2.setUser(testUser);
        testRefreshToken2.setExpiryDate(Instant.now().minusMillis(20000));
        testRefreshToken2.setToken(UUID.randomUUID().toString());
        this.testToken2 = testRefreshToken2;
        log.info("[StartedTest]");
    }
    @After
    public void after(){
        log.info("[EndedTest]");
    }

    @Test
    public void findByTokenWithOptionalReturnTest() throws Exception{
        // arrange
        Optional<RefreshToken> testOptional = Optional.of(testToken1);
        Mockito.when(refreshTokenRepository.findByToken(testToken1.getToken())).thenReturn(testOptional);
        // act
        Optional<RefreshToken> returnedOptional = refreshTokenService.findByToken(testToken1.getToken());
        // assert
        assertTrue(returnedOptional.isPresent());
    }

    @Test
    public void findByTokenWithNullOptionalReturnTest() throws Exception{
        // arrange
        Optional<RefreshToken> testOptional = Optional.empty();
        Mockito.when(refreshTokenRepository.findByToken(testToken1.getToken())).thenReturn(testOptional);
        // act
        Optional<RefreshToken> returnedOptional = refreshTokenService.findByToken(testToken1.getToken());
        // assert
        assertFalse(returnedOptional.isPresent());
    }

    @Test
    public void getByTokenTest() throws Exception{
        // arrange
        Mockito.when(refreshTokenRepository.getByToken(testToken1.getToken())).thenReturn(testToken1);
        // act
        RefreshToken refreshToken = refreshTokenService.getByToken(testToken1.getToken());
        // assert
        boolean result = refreshToken != null;
        assertTrue(result);
    }

    @Test(expected = TokenRefreshException.class)
    public void getByTokenWithNotExistingTokenTest() throws Exception{
        // arrange
        Mockito.when(refreshTokenRepository.getByToken(testToken1.getToken())).thenReturn(null);
        // act
        RefreshToken refreshToken = refreshTokenService.getByToken(testToken1.getToken());
        // assert
    }

    @Test
    public void createRefreshTokenTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(testToken1.getUser());
        Mockito.when(refreshTokenRepository.save(ArgumentMatchers.any())).thenReturn(testToken1);
        // act
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(1);
        // assert
        boolean result = refreshToken != null;
        assertTrue(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void createRefreshTokenNoUserFoundTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(null);
        // act
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(1);
        // assert
    }

    @Test
    public void verifyExpirationTest() throws Exception{
        // arrange
        // act
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(testToken1);
        // assert
        boolean result = refreshToken != null;
        assertTrue(result);
    }

    @Test(expected = TokenRefreshException.class)
    public void verifyExpirationWithExpTokenTest() throws Exception{
        // arrange
        // act
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(testToken2);
        // assert
    }

    @Test
    public void deleteByUserIdTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(testToken1.getUser());
        Mockito.when(refreshTokenRepository.deleteByUser(testToken1.getUser())).thenReturn(1);
        // act
        int deletedRecords = refreshTokenService.deleteByUserId(1);
        // assert
        assertEquals(1, deletedRecords);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteByUserIdWithNotExistingUserTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(null);
        // act
        int deletedRecords = refreshTokenService.deleteByUserId(1);
        // assert
    }

    @Test
    public void getUserInformationByTokenTest() throws Exception{
        // arrange
        Mockito.when(refreshTokenRepository.getByToken(testToken1.getToken())).thenReturn(testToken1);
        // act
        User returnedUser = refreshTokenService.getUserInformationByToken(testToken1.getToken());
        // assert
        assertNotNull(returnedUser);
    }

    @Test(expected = InvalidInputException.class)
    public void getUserInformationByTokenWithEmptyTest() throws Exception{
        // arrange
        // act
        refreshTokenService.getUserInformationByToken("");
        // assert
    }

    @Test(expected = InvalidUUIDPatternException.class)
    public void getUserInformationByTokenWithMalformedUUIDTest() throws Exception{
        // arrange
        // act
        refreshTokenService.getUserInformationByToken("iAmInvalid");
        // assert
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserInformationByTokenWithNotFoundTest() throws Exception{
        // arrange
        Mockito.when(refreshTokenRepository.getByToken(testToken1.getToken())).thenReturn(null);
        // act
        User returnedUser = refreshTokenService.getUserInformationByToken(testToken1.getToken());
        // assert
    }
}
