package nl.fhict.digitalmarketplace.repository.user;

import nl.fhict.digitalmarketplace.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User getById(Integer id);
    boolean existsByEmail(String email);
}
