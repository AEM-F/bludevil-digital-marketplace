package nl.fhict.digitalmarketplace;

import com.zaxxer.hikari.HikariDataSource;
import nl.fhict.digitalmarketplace.config.DatabaseConfig;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import nl.fhict.digitalmarketplace.repository.user.RoleRepository;
import nl.fhict.digitalmarketplace.service.user.AdminAccountGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootApplication
public class DigitalmarketplaceApplication implements CommandLineRunner {

    protected RoleRepository roleRepository;
    protected GenreRepository genreRepository;
    protected ProductPlatformRepository platformRepository;
    protected AdminAccountGenerator adminAccountGenerator;
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
    public void setAdminAccountGenerator(AdminAccountGenerator adminAccountGenerator) {
        this.adminAccountGenerator = adminAccountGenerator;
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
//        insertDataTest();
    }
}
