package nl.fhict.digitalmarketplace.service.user;

import nl.fhict.digitalmarketplace.model.jwt.RefreshToken;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.jwt.RefreshTokenRepository;
import nl.fhict.digitalmarketplace.repository.user.RoleRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminAccountGenerator {
    @Value("${bludevil.default-admin.email}")
    private String defaultAdminEmail;
    @Value("${bludevil.default-admin.password}")
    private String defaultAdminPassword;
    private Logger logger = LoggerFactory.getLogger(AdminAccountGenerator.class);
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public AdminAccountGenerator(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, RefreshTokenRepository refreshTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void buildAdminAccount(){
        boolean areAdminAccount = this.checkForAdminAccounts();
        if(areAdminAccount){
            logger.info("Checking for default account");
            User adminAccount = userRepository.findByEmail(this.defaultAdminEmail);
            long adminCount= userRepository.countAllByActiveEqualsAndRolesName(true, ERole.ROLE_ADMIN);
            if(adminAccount != null && adminCount > 1){
                logger.info("The default account was found, removing account");
                this.deleteAdminAccount(adminAccount);
            }else{
                logger.info("The default account was not found or is the only admin account");
            }
        }else{
            logger.info("Creating default user with email: "+this.defaultAdminEmail+" and password: "+this.defaultAdminPassword);
            User defaultAdmin = new User(this.defaultAdminEmail, this.passwordEncoder.encode(this.defaultAdminPassword));
            logger.info("Setting roles");
            List<Role> roles = new ArrayList<>();
            roles.add(this.roleRepository.findByName(ERole.ROLE_ADMIN));
            defaultAdmin.setRoles(roles);
            this.userRepository.save(defaultAdmin);
            logger.info("Saved default admin to DB");
        }
    }

    private boolean checkForAdminAccounts(){
        logger.info("Checking active admin accounts...");
        long activeAccounts = userRepository.countAllByActiveEqualsAndRolesName(true, ERole.ROLE_ADMIN);
        logger.info("Found active admin accounts: "+activeAccounts);
        return activeAccounts >= 1;
    }

    private void deleteAdminAccount(User defaultAdmin){
        logger.info("Attempting to delete default account...");
        logger.info("Checking default account refresh tokens");
        long foundTokensNr = this.refreshTokenRepository.countAllByUser_Id(defaultAdmin.getId());
        logger.info("Found "+foundTokensNr+" refresh tokens");
        if (foundTokensNr > 0){
            logger.info("Removing all refresh tokens");
            this.refreshTokenRepository.deleteAllByUser_Id(defaultAdmin.getId());
        }
        logger.info("Deleting default account");
        this.userRepository.delete(defaultAdmin);
        logger.info("Deleted successfully");
    }
}
