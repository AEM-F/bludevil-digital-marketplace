package nl.fhict.digitalmarketplace.controller.user;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.service.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/users")
public class UserController {
    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getUserById(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException, InvalidInputException {
        User returnedUser = userService.getById(id);
        return ResponseEntity.ok(returnedUser);
    }
}
