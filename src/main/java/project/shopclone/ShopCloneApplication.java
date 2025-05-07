package project.shopclone;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;

import java.util.TimeZone;

@EnableMongoAuditing
@EnableMongoRepositories
@EnableRetry
@EnableJpaAuditing
@EnableCaching
@SpringBootApplication // (exclude = SecurityAutoConfiguration.class)
public class ShopCloneApplication {

    @PostConstruct
    void started() { // 스프링 타임존 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ShopCloneApplication.class, args);
    }

}
