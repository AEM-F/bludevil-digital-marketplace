package nl.fhict.digitalmarketplace;

import nl.fhict.digitalmarketplace.model.product.*;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class DigitalmarketplaceApplication implements CommandLineRunner {

    private Logger LOG = LoggerFactory.getLogger(DigitalmarketplaceApplication.class);
    private ProductPlatformRepository productPlatformRepository;
    private GenreRepository genreRepository;
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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
        String[] productNames = {"origin","steam","battle.net", "ncsoft", "uplay", "xbox", "playstation", "android", "gog", "nintendo", "epic", "microsoft"};

        List<ProductPlatform> productPlatforms = new ArrayList<>();
        for (String name : productNames){
            ProductPlatform platform = new ProductPlatform(name);
            productPlatforms.add(platform);
        }

        LOG.info("Saving default product platforms");
        productPlatformRepository.saveAll(productPlatforms);

        String[] genreNames = {"action", "shooter","survival","battle_royal","adventure","horror","rpg","racing", "sports","strategy","sandbox","open_world"};
        List<Genre> genres = new ArrayList<>();
        for (String name : genreNames){
            Genre genre = new Genre(name);
            genres.add(genre);
        }

        LOG.info("Saving default genres");
        genreRepository.saveAll(genres);

        //test-to-remove
//         LocalDate testLocalDate = LocalDate.now();
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//         String testDate = formatter.format(testLocalDate);
//        ProductPlatform testPlatform1 = new ProductPlatform(1, "origin");
//        Genre testGenre = new Genre(1, "action");
//        List<Genre> genress = new ArrayList<>();
//        genress.add(testGenre);
//        Product product1 = new VideoGame("Antem", testPlatform1, 0, 39.99, "Cool game", "smh/inapp", "On Windows", true, testDate, genress );
//        productRepository.save(product1);
//
//        Product product2 = new SoftwareProduct("Olaunch", testPlatform1, 0, 20.99, "Cool software", "fuk/inapp", "On windows", true);
//        productRepository.save(product2);
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }
}
