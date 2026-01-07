package network.ranked.backend.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SslOptions;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author JustReddy
 */
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        final RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(
                host,
                port
        );

        if (password != null && !password.isBlank()) {
            configuration.setPassword(password);
        }

        // This is insecure. I know, don't care, everything is localhosted for me anyway
        final SslOptions sslOptions = SslOptions.builder().
                trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        final ClientOptions clientOptions = ClientOptions.builder()
                .sslOptions(sslOptions)
                .build();

        final LettuceClientConfiguration clientConfig =
                LettuceClientConfiguration.builder()
                        .clientOptions(clientOptions)
                        .useSsl()
                        .build();

        return new LettuceConnectionFactory(configuration, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

}
