package nl.fhict.digitalmarketplace.service.user;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.user.RoleRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@Service
public class UserService implements IUserService{

    private Logger log = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository= roleRepository;
    }


    @Override
    public User createUser(@Valid User user) throws InvalidInputException {
        log.info("Validating the email: "+user.getEmail());
        User foundUser = userRepository.findByEmail(user.getEmail());
        if(foundUser == null){
            log.info("Email is valid, no user with the given email was found");
            log.info("Saving user...");
            userRepository.save(user);
            return user;
        }
        throw new InvalidInputException("The given email is already in use");
    }

    @Override
    public User getById(Integer id) throws ResourceNotFoundException, InvalidInputException {
        log.info("Validating the given id: "+id);
        if(id > 0){
            log.info("Getting the user with given id: "+id);
            User foundUser = userRepository.getById(id);
            if(foundUser != null){
                log.info("Successfully returned the user");
                return foundUser;
            }
            throw new ResourceNotFoundException("The user with the given id was not found");
        }
        throw new InvalidInputException("The given id is not valid");
    }

    @Override
    public User updateUser(@Valid User user, Integer userId) throws InvalidInputException, ResourceNotFoundException {
        if(userId > 0){
            log.info("Validating the email: "+user.getEmail());
            User returnedUser = userRepository.getById(userId);
            if(returnedUser != null){
                if(returnedUser.getEmail().equals(user.getEmail())){
                    user.setId(userId);
                    return userRepository.save(user);
                }
                else{
                    User checkUser = userRepository.findByEmail(user.getEmail());
                    if(checkUser == null){
                        log.info("Email is valid, no user with the given email was found");
                        log.info("Saving user...");
                        user.setId(userId);
                        userRepository.save(user);
                        return user;
                    } else{
                        throw new InvalidInputException("The given email is already in use");
                    }
                }
            }else{
                throw new ResourceNotFoundException("No user was found with the given id");
            }
        }
        else {
            throw new InvalidInputException("The given id is invalid");
        }
    }
}
