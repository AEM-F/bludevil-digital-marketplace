package nl.fhict.digitalmarketplace.repository.user;

import nl.fhict.digitalmarketplace.model.user.UserWebView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserWebViewRepository extends JpaRepository<UserWebView, Integer> {
    long countAllByCreationDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
