package nl.fhict.digitalmarketplace.IntegrationTests.user;

import nl.fhict.digitalmarketplace.IntegrationTests.AbstractTest;
import nl.fhict.digitalmarketplace.config.security.jwt.JwtUtils;
import nl.fhict.digitalmarketplace.model.jwt.RefreshToken;
import nl.fhict.digitalmarketplace.model.request.LoginRequest;
import nl.fhict.digitalmarketplace.model.request.SignupRequest;
import nl.fhict.digitalmarketplace.model.request.TokenRefreshRequest;
import nl.fhict.digitalmarketplace.model.response.JwtResponse;
import nl.fhict.digitalmarketplace.model.response.TokenRefreshResponse;
import nl.fhict.digitalmarketplace.model.response.UserDetailsResponse;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.jwt.RefreshTokenRepository;
import nl.fhict.digitalmarketplace.repository.user.RoleRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import nl.fhict.digitalmarketplace.service.jwt.RefreshTokenService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest extends AbstractTest {
    private User testUser;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    @Before
    public void setUp(){
        super.setUp();
        List<Role> roleList = new ArrayList<>();
        Role testRole = new Role(ERole.ROLE_USER);
        testRole.setId(1);
        roleList.add(testRole);
        // prepare test user
        User testUser = new User();
        testUser.setActive(true);
        testUser.setCreationDate(LocalDateTime.now());
        testUser.setEmail("test@gmail.com");
        testUser.setFirstName("Testy");
        testUser.setLastName("Tester");
        testUser.setPassword(passwordEncoder.encode("secret"));
        testUser.setImagePath("");
        testUser.setRoles(roleList);
        userRepository.save(testUser);
        // prepare inactive test user
        User testUser2 = new User();
        testUser2.setActive(false);
        testUser2.setCreationDate(LocalDateTime.now());
        testUser2.setEmail("test2@gmail.com");
        testUser2.setFirstName("Testy");
        testUser2.setLastName("Tester");
        testUser2.setPassword(passwordEncoder.encode("secret"));
        testUser2.setImagePath("");
        testUser2.setRoles(roleList);
        userRepository.save(testUser2);
        this.testUser = testUser;
    }

//    @Test
//    public void testRep() throws Exception{
//        List<User> users = this.userRepository.findByRolesName(ERole.ROLE_USER);
//        boolean result = false;
//        if(users.size() > 0){
//            result = true;
//        }
//        assertTrue(result);
//    }

    @Test
    public void testRepo2() throws Exception{
        Pageable requestedPage = PageRequest.of(0, 5);
        Page<User> userPage = this.userRepository.findByActiveEqualsAndRolesName(true,ERole.ROLE_USER, requestedPage);
        assertNotNull(userPage);
    }

    @Test
    public void signInUserTest() throws Exception{
        // arrange
        String uri = "http://localhost:8080/api/auth/signin";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("secret");
        String inputContent = mapToJson(loginRequest);
        // act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE).content(inputContent)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();
        JwtResponse jwtResponse = mapFromJson(content, JwtResponse.class);
        // assert
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotNull(jwtResponse.getId());
        assertNotNull(jwtResponse.getRefreshToken());
        assertNotNull(jwtResponse.getToken());
        assertNotNull(jwtResponse.getRefreshTokenExp());
    }

    @Test()
    public void signInUserWrongCredentials() throws Exception{
        // arrange
        String message = "";
        String uri = "http://localhost:8080/api/auth/signin";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testing@gmail.com");
        loginRequest.setPassword("secret");
        String inputContent = mapToJson(loginRequest);
        // act
        try {
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE).content(inputContent)).andReturn();
        }catch (NestedServletException e){
            message = e.getRootCause().getMessage();
        }
        // assert
        assertEquals("Bad credentials", message);
    }

    @Test
    public void signInInactiveUserTest() throws Exception{
        // arrange
        String message = "";
        String uri = "http://localhost:8080/api/auth/signin";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test2@gmail.com");
        loginRequest.setPassword("secret");
        String inputContent = mapToJson(loginRequest);
        // act
        try {
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE).content(inputContent)).andReturn();
        }catch (NestedServletException e){
            message = e.getRootCause().getMessage();
        }
        // assert
        assertEquals("User is disabled", message);
    }

    @Test
    public void signupTest() throws Exception{
        // arrange
        String uri = "http://localhost:8080/api/auth/signup";
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("thing@gmail.com");
        signupRequest.setPassword("secret");
        String inputContent = mapToJson(signupRequest);
        // act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE).content(inputContent)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        // assert
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void signupWithExistingEmailTest() throws Exception{
        // arrange
        String uri = "http://localhost:8080/api/auth/signup";
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@gmail.com");
        signupRequest.setPassword("secret");
        String inputContent = mapToJson(signupRequest);
        // act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE).content(inputContent)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        // assert
        assertEquals(422, mvcResult.getResponse().getStatus());
    }

    @Test
    public void refreshAccessTokenWithRefreshTokenTest() throws Exception{
        // arrange
        String endpoint = "http://localhost:8080/api/auth/refreshToken";
        String jwtToken = jwtUtils.generateTokenFromUsername("test@gmail.com");
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(1);
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken(refreshToken.getToken());
        String jsonRequest = super.mapToJson(refreshRequest);
        // act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, jwtToken).content(jsonRequest)).andReturn();
        // assert
        String content = mvcResult.getResponse().getContentAsString();
        TokenRefreshResponse tokenRefreshResponse = mapFromJson(content, TokenRefreshResponse.class);
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotNull(tokenRefreshResponse.getRefreshToken());
        assertNotNull(tokenRefreshResponse.getToken());
        assertNotNull(tokenRefreshResponse.getRefreshTokenExp());
    }

    @Test
    public void refreshAccessTokenWithExpiredRefreshTokenTest() throws Exception{
        // arrange
        String endpoint = "http://localhost:8080/api/auth/refreshToken";
        String jwtToken = jwtUtils.generateTokenFromUsername(testUser.getEmail());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(testUser);
        refreshToken.setExpiryDate(Instant.now().minusMillis(20000));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken(refreshToken.getToken());
        String jsonRequest = super.mapToJson(refreshRequest);
        // act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, jwtToken).content(jsonRequest)).andReturn();
        // assert
        assertEquals(417, mvcResult.getResponse().getStatus());
    }

    @Test
    public void refreshAccessTokenWithMalformedRefreshTokenTest() throws Exception{
        // arrange
        String endpoint = "http://localhost:8080/api/auth/refreshToken";
        String jwtToken = jwtUtils.generateTokenFromUsername(testUser.getEmail());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(testUser);
        refreshToken.setExpiryDate(Instant.now().minusMillis(20000));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("legit");
        String jsonRequest = super.mapToJson(refreshRequest);
        // act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, jwtToken).content(jsonRequest)).andReturn();
        // assert
        assertEquals(417, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser()
    public void getLoggedUserInformationTest() throws Exception{
        // arrange
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(1);
        String endpoint = "http://localhost:8080/api/auth/userInfo/" + refreshToken.getToken();
        // act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        // assert
        String content = mvcResult.getResponse().getContentAsString();
        UserDetailsResponse userDetailsResponse = mapFromJson(content, UserDetailsResponse.class);
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(testUser.getFirstName(), userDetailsResponse.getFirstName());
        assertEquals(testUser.getEmail(), userDetailsResponse.getEmail());
        assertEquals(testUser.getLastName(), userDetailsResponse.getLastName());
        assertEquals(testUser.getImagePath(), userDetailsResponse.getImageUrl());
        assertEquals("MEM", userDetailsResponse.getRoles().get(0));
    }
}
