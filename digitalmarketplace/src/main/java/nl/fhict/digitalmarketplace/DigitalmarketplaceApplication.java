package nl.fhict.digitalmarketplace;

import nl.fhict.digitalmarketplace.model.product.*;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DigitalmarketplaceApplication implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(DigitalmarketplaceApplication.class);
    private ProductPlatformRepository productPlatformRepository;
    private GenreRepository genreRepository;

    @Autowired
    public void setGenreRepository(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Autowired
    public void setProductPlatformRepository(ProductPlatformRepository productPlatformRepository) {
        this.productPlatformRepository = productPlatformRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DigitalmarketplaceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String[] platformNames = {"origin","steam","battle.net", "ncsoft", "uplay", "xbox", "playstation", "android", "gog", "nintendo", "epic", "microsoft"};

        List<ProductPlatform> productPlatforms = new ArrayList<>();
        for (String name : platformNames){
            ProductPlatform platform = new ProductPlatform(name);
            productPlatforms.add(platform);
        }

        log.info("Saving default product platforms");
        productPlatformRepository.saveAll(productPlatforms);

        String[] genreNames = {"action", "shooter","survival","battle_royal","adventure","horror","rpg","racing", "sports","strategy","sandbox","open_world"};
        List<Genre> genres = new ArrayList<>();
        for (String name : genreNames){
            Genre genre = new Genre(name);
            genres.add(genre);
        }

        log.info("Saving default genres");
        genreRepository.saveAll(genres);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
