package project.shopclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication // (exclude = SecurityAutoConfiguration.class)
public class ShopCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopCloneApplication.class, args);
    }

}
