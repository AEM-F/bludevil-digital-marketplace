package nl.fhict.digitalmarketplace;

import com.zaxxer.hikari.HikariDataSource;
import nl.fhict.digitalmarketplace.config.DatabaseConfig;
import nl.fhict.digitalmarketplace.model.product.*;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import nl.fhict.digitalmarketplace.repository.user.RoleRepository;
import nl.fhict.digitalmarketplace.service.img.IImageService;
import nl.fhict.digitalmarketplace.service.img.ImageService;
import nl.fhict.digitalmarketplace.service.user.AdminAccountGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootApplication
public class DigitalmarketplaceApplication implements CommandLineRunner {

    protected ProductRepository productRepository;
    protected RoleRepository roleRepository;
    protected GenreRepository genreRepository;
    protected ProductPlatformRepository platformRepository;
    protected AdminAccountGenerator adminAccountGenerator;
    protected IImageService imageService;
    private static Logger log = LoggerFactory.getLogger(DigitalmarketplaceApplication.class);

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setGenreRepository(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Autowired
    public void setPlatformRepository(ProductPlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Autowired
    public void setImageService(IImageService imageService) {
        this.imageService = imageService;
    }

    @Autowired
    public void setAdminAccountGenerator(AdminAccountGenerator adminAccountGenerator) {
        this.adminAccountGenerator = adminAccountGenerator;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DigitalmarketplaceApplication.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            HikariDataSource ds = DatabaseConfig.ds;
            if (ds != null){
                try {
                    ds.getConnection().close();
                }
                catch (SQLException e){
                    log.error(e.getMessage());
                }
            }
        }, "Shutdown-thread"));
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    public static boolean isUUID(String str){
        Pattern p = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        return p.matcher(str).matches();
    }
    private void checkStoredRoles(){
        Role roleUser = roleRepository.getById(1);
        if (roleUser == null){
            roleUser = new Role();
            roleUser.setName(ERole.ROLE_USER);
            roleRepository.save(roleUser);
        }
        Role roleAdmin = roleRepository.getById(2);
        if (roleAdmin == null){
            roleAdmin = new Role();
            roleAdmin.setName(ERole.ROLE_ADMIN);
            roleRepository.save(roleAdmin);
        }
    }

    public void insertDataTest(){
        String[] platformNames = {"origin","steam","battle.net", "ncsoft", "uplay", "xbox", "playstation", "android", "gog", "nintendo", "epic", "microsoft"};
        List<ProductPlatform> productPlatforms = new ArrayList<>();
        for (String name : platformNames){
            ProductPlatform platform = new ProductPlatform(name);
            productPlatforms.add(platform);
        }
        platformRepository.saveAll(productPlatforms);
        String[] genreNames = {"action", "shooter","survival","battle_royal","adventure","horror","rpg","racing", "sports","strategy","sandbox","open_world"};
        List<Genre> genres = new ArrayList<>();
        for (String name : genreNames){
            Genre genre = new Genre(name);
            genres.add(genre);
        }
        genreRepository.saveAll(genres);
    }

    @Override
    public void run(String... args) throws Exception {
        checkStoredRoles();
        this.adminAccountGenerator.buildAdminAccount();
        this.imageService.addImageNotAvailableFile();
//        insertDataTest();
//        this.createProductsForDemo();
    }

    private void createProductsForDemo(){
        ProductPlatform testPlatform1 = new ProductPlatform(1, "origin");
        Genre testGenre = new Genre(1, "action");
        List<Genre> testGenres = new ArrayList<>();
        testGenres.add(testGenre);
        Date testLocalDate = new Date();
        VideoGame product1 = new VideoGame();
        product1.setName("Resident Evil Village");
        product1.setProductPlatform(testPlatform1);
        product1.setQuantity(0);
        product1.setPrice(50);
        product1.setDescription("Set a few years after the horrifying events in the critically acclaimed Resident Evil 7 biohazard, the all-new storyline begins with Ethan Winters and his wife Mia living peacefully in a new location, free from their past nightmares. Just as they are building their new life together, tragedy befalls them once again.");
        product1.setSystemRequirements("TBA");
        product1.setActive(true);
        product1.setReleaseDate(testLocalDate);
        product1.setGenres(testGenres);
        product1.setImageUrl("https://static1.thegamerimages.com/wordpress/wp-content/uploads/2021/04/Village-Frame-Rate-Resolution-1.jpg");
        productRepository.save(product1);

        VideoGame product2 = new VideoGame();
        product2.setName("Call of Duty: Black Ops Cold War Green");
        product2.setProductPlatform(testPlatform1);
        product2.setQuantity(0);
        product2.setPrice(40);
        product2.setDescription("Black Ops Cold War will drop fans into the depths of the Cold War’s volatile geopolitical battle of the early 1980s. Nothing is ever as it seems in a gripping single-player Campaign, where players will come face-to-face with historical figures and hard truths, as they battle around the globe through iconic locales like East Berlin, Vietnam, Turkey, Soviet KGB headquarters and more.");
        product2.setSystemRequirements("TBA");
        product2.setActive(true);
        product2.setReleaseDate(testLocalDate);
        product2.setGenres(testGenres);
        product2.setImageUrl("https://images.pushsquare.com/b78d155f4c129/call-of-duty-black-ops-cold-war-ps5-ps4.original.jpg");
        productRepository.save(product2);

        SoftwareProduct product3 = new SoftwareProduct();
        product3.setName("Windows 10 Professional OEM Key");
        product3.setProductPlatform(testPlatform1);
        product3.setQuantity(0);
        product3.setPrice(26);
        product3.setDescription("Windows 10 pro is the latest version of the popular operating system. After the latest updates it presents more of a customized interface that recognizes the device's hardware. Put simply, it’s a system you can use on any type of machine. Furthermore, it is safe and fast: just what any professional, gamer or student needs, especially when complimented with the latest Ms Office package available here.");
        product3.setSystemRequirements("TBA");
        product3.setActive(true);
        product3.setImageUrl("https://msofficeworks.com/wp-content/uploads/2017/06/win-10-pro-64-32-bits-msofficeworks.jpg");
        productRepository.save(product3);
    }
}
