package nl.fhict.digitalmarketplace.controller.user;

import nl.fhict.digitalmarketplace.config.security.UserDetailsImpl;
import nl.fhict.digitalmarketplace.config.security.jwt.JwtUtils;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.InvalidUUIDPatternException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.customException.TokenRefreshException;
import nl.fhict.digitalmarketplace.model.jwt.RefreshToken;
import nl.fhict.digitalmarketplace.model.request.LoginRequest;
import nl.fhict.digitalmarketplace.model.request.SignupRequest;
import nl.fhict.digitalmarketplace.model.request.TokenRefreshRequest;
import nl.fhict.digitalmarketplace.model.request.UserDetailsRequest;
import nl.fhict.digitalmarketplace.model.response.JwtResponse;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import nl.fhict.digitalmarketplace.model.response.TokenRefreshResponse;
import nl.fhict.digitalmarketplace.model.response.UserDetailsResponse;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.user.RoleRepository;
import nl.fhict.digitalmarketplace.service.jwt.IRefreshTokenService;
import nl.fhict.digitalmarketplace.service.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private IRefreshTokenService refreshTokenService;
    private IUserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          IRefreshTokenService refreshTokenService,
                          IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws ResourceNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                LocalDateTime.ofInstant(refreshToken.getExpiryDate(), ZoneOffset.UTC)));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws InvalidInputException {
        User user = new User(signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()));
        List<Role> roles = new ArrayList<>();
        Role memberRole = roleRepository.findByName(ERole.ROLE_USER);
        roles.add(memberRole);
        user.setRoles(roles);
        userService.createUser(user);

        return ResponseEntity.ok(new MessageDTO("User registered successfully!", "INFO", "api/auth/signup"));
    }

    @PostMapping(path = "/refreshToken")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody TokenRefreshRequest request) throws TokenRefreshException {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.getByToken(requestRefreshToken);
        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String accessToken = jwtUtils.generateTokenFromUser(user);
        return ResponseEntity.ok(new TokenRefreshResponse(accessToken, refreshToken.getToken(), LocalDateTime.ofInstant(refreshToken.getExpiryDate(), ZoneOffset.UTC)));
    }

    @GetMapping(path = "/userInfo/{token}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getLoggedUserInformation(@PathVariable(name = "token") String refreshToken) throws InvalidInputException, InvalidUUIDPatternException, ResourceNotFoundException, TokenRefreshException {
        User returnedUser = refreshTokenService.getUserInformationByToken(refreshToken);
        List<String> roles = returnedUser.getRoles().stream().map(role -> {
            if(role.getName() == ERole.ROLE_USER){
                return "MEM";
            }
            else if(role.getName() == ERole.ROLE_ADMIN){
                return "ADM";
            }
            else return "";
        }).collect(Collectors.toList());
        UserDetailsResponse userinfo = new UserDetailsResponse(returnedUser.getFirstName(), returnedUser.getLastName(), returnedUser.getEmail(), roles, returnedUser.getImagePath());
        return ResponseEntity.ok(userinfo);
    }

    @PutMapping(path = "/updateInfo")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> updateUserDetails(@RequestBody @Valid UserDetailsRequest detailsRequest) throws InvalidUUIDPatternException, TokenRefreshException, ResourceNotFoundException, InvalidInputException {
        User returnedUser = refreshTokenService.getUserInformationByToken(detailsRequest.getRefreshToken());
        returnedUser.setEmail(detailsRequest.getEmail());
        returnedUser.setFirstName(detailsRequest.getFirstName());
        returnedUser.setLastName(detailsRequest.getLastName());
        User updatedUser = userService.updateUser(returnedUser, returnedUser.getId());
        String accessToken = jwtUtils.generateTokenFromUser(updatedUser);
        HashMap<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", accessToken);
        return ResponseEntity.ok(responseBody);
    }
}
