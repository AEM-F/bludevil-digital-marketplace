package nl.fhict.digitalmarketplace.controller.user;

import nl.fhict.digitalmarketplace.config.security.jwt.JwtUtils;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.service.jwt.IRefreshTokenService;
import nl.fhict.digitalmarketplace.service.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "api/users")
public class UserController {
    private JwtUtils jwtUtils;
    private IUserService userService;
    private IRefreshTokenService refreshTokenService;

    public UserController(JwtUtils jwtUtils, IUserService userService, IRefreshTokenService refreshTokenService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getUserById(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException, InvalidInputException {
        User returnedUser = userService.getById(id);
        return ResponseEntity.ok(returnedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/statistics/userRoleRatio")
    public ResponseEntity<Object> getUserRoleRatio(){
        return ResponseEntity.ok(userService.getUserRoleRatio());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/statistics/countAll")
    public ResponseEntity<Object> countAllUsers(){
        return ResponseEntity.ok(userService.countAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/statistics/dailyRegistered")
    public ResponseEntity<Object> getDailyRegisteredUsers(){
        return ResponseEntity.ok(userService.getDailyRegisteredUsers());
    }

}
