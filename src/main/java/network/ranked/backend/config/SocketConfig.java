package network.ranked.backend.config;

import network.ranked.backend.socket.acceptor.SetupAuthSocketAcceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;

/**
 * @author JustReddy
 */
@Configuration

public class SocketConfig {

    @Value("${rsocket.auth.token}")
    private String expectedToken;


    @Bean
    public RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRSocketStrategies(strategies);
        return handler;
    }


    @Bean
    public RSocketServerCustomizer rSocketServerCustomizer(RSocketMessageHandler handler) {
        return server -> server.acceptor(new SetupAuthSocketAcceptor(expectedToken, handler));
    }

}
