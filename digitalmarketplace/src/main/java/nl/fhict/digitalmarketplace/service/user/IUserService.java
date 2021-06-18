package nl.fhict.digitalmarketplace.service.user;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.response.ContactResponse;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.model.response.StatisticsItemResponse;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface IUserService {
    User createUser(@Valid User user) throws InvalidInputException;
    User getById(Integer id) throws ResourceNotFoundException, InvalidInputException;
    User updateUser(@Valid User user, Integer userId) throws InvalidInputException, ResourceNotFoundException;
    PaginationResponse<ContactResponse> findContacts(int page, int size, ERole role, boolean ignoreRole) throws InvalidInputException;
    List<StatisticsItemResponse> getUserRoleRatio();
    StatisticsItemResponse countAllUsers();
    StatisticsItemResponse getDailyRegisteredUsers();
    ContactResponse findContactById(Integer id) throws ResourceNotFoundException, InvalidInputException;
}
