package nl.fhict.digitalmarketplace.service.jwt;

import nl.fhict.digitalmarketplace.DigitalmarketplaceApplication;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.InvalidUUIDPatternException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.customException.TokenRefreshException;
import nl.fhict.digitalmarketplace.model.jwt.RefreshToken;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.jwt.RefreshTokenRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {
    @Value("${bludevil.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;
    private Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken getByToken(String token) throws TokenRefreshException {
        RefreshToken token1 = refreshTokenRepository.getByToken(token);
        if (token1 != null){
            return token1;
        }
        throw new TokenRefreshException(token, "No token was found in the DB");
    }

    @Override
    public RefreshToken createRefreshToken(Integer userId) throws ResourceNotFoundException {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.getById(userId);
        if (user != null){
            refreshToken.setUser(user);
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
        throw new ResourceNotFoundException("No user was found with the given id: "+userId);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            log.error("Token is expired, removing token and throwing exception");
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Override
    @Transactional
    public Integer deleteByUserId(Integer userId) throws ResourceNotFoundException {
        User user = userRepository.getById(userId);
        if (user != null){
            return refreshTokenRepository.deleteByUser(user);
        }
        throw new ResourceNotFoundException("No user was found with the given id: "+userId);
    }

    @Override
    @Transactional
    public User getUserInformationByToken(String refreshToken) throws InvalidInputException, InvalidUUIDPatternException, ResourceNotFoundException, TokenRefreshException {
        if(refreshToken != null && !refreshToken.isEmpty()){
            if(DigitalmarketplaceApplication.isUUID(refreshToken)){
                RefreshToken foundToken = refreshTokenRepository.getByToken(refreshToken);
                if(foundToken != null){
                    RefreshToken validatedToken = verifyExpiration(foundToken);
                    return validatedToken.getUser();
                }
                throw new ResourceNotFoundException("The given token cannot be found");
            }
            throw new InvalidUUIDPatternException("The given token is malformed");
        }
        throw new InvalidInputException("The given token cannot be empty");
    }
}
