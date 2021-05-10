package nl.fhict.digitalmarketplace.repository.jwt;

import nl.fhict.digitalmarketplace.model.jwt.RefreshToken;
import nl.fhict.digitalmarketplace.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken , Integer> {
    @Override
    Optional<RefreshToken> findById(Integer id);
    Optional<RefreshToken> findByToken(String token);
    Integer deleteByUser(User user);
    RefreshToken getByToken(String token);
}
