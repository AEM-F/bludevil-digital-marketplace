package nl.fhict.digitalmarketplace.repository.user;

import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User getById(Integer id);
    boolean existsByEmail(String email);
    Page<User> findAllByActiveEquals(boolean accountState, Pageable pageable);
    Page<User> findByActiveEqualsAndRolesName(boolean accountState,ERole eRole, Pageable pageable);
}
