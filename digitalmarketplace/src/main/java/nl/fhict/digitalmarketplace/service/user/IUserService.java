package nl.fhict.digitalmarketplace.service.user;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.user.User;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface IUserService {
    User createUser(@Valid User user) throws InvalidInputException;
    User getById(Integer id) throws ResourceNotFoundException, InvalidInputException;
}
