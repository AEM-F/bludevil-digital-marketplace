package nl.fhict.digitalmarketplace.service.jwt;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.InvalidUUIDPatternException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.jwt.RefreshToken;
import nl.fhict.digitalmarketplace.model.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface IRefreshTokenService {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Integer userId) throws ResourceNotFoundException;

    RefreshToken verifyExpiration(RefreshToken token);

    @Transactional
    Integer deleteByUserId(Integer userId) throws ResourceNotFoundException;
    @Transactional
    public User getUserInformationByToken(String refreshToken) throws InvalidInputException, InvalidUUIDPatternException, ResourceNotFoundException;
}
