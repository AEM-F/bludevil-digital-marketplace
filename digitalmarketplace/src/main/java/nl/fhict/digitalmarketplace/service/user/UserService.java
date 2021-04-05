package nl.fhict.digitalmarketplace.service.user;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@Service
public class UserService implements IUserService{

    private Logger log = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
   // private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User CreateUser(@Valid User user) throws InvalidInputException {
        log.info("Validating the email: "+user.getEmail());
        User foundUser = userRepository.findByEmail(user.getEmail());
        if(foundUser == null){
            log.info("Email is valid, no user with the given email was found");
           // user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            log.info("Saving user...");
            userRepository.save(user);
            return user;
        }
        throw new InvalidInputException("The given email is already in use");
    }
}
