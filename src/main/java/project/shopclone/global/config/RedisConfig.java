package project.shopclone.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {
    @Value("${jwt.redis.host}")
    private String host;

    @Value("${jwt.redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() { // Lettuce - 토큰, 캐싱에 활용
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedissonClient redissonClient() { // Redisson - 분산락 활용
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port )
//                .setConnectionPoolSize(20)
//                .setDatabase(0)
                .setConnectionMinimumIdleSize(10)
                .setTimeout(5000);
        return Redisson.create(config);
    }
}