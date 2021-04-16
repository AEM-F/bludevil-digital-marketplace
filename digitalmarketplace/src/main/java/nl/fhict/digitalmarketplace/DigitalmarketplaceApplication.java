package nl.fhict.digitalmarketplace;

import com.zaxxer.hikari.HikariDataSource;
import nl.fhict.digitalmarketplace.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class DigitalmarketplaceApplication{

    private static Logger log = LoggerFactory.getLogger(DigitalmarketplaceApplication.class);

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
}
