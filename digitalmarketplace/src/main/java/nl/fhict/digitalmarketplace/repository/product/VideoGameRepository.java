package nl.fhict.digitalmarketplace.repository.product;

import nl.fhict.digitalmarketplace.model.product.VideoGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface VideoGameRepository extends JpaRepository<VideoGame, Integer> {
    long countAllByGenresGenreName(String name);
    Page<VideoGame> findAllByReleaseDateBetween(Date start, Date end, Pageable pageable);
}
