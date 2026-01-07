package network.ranked.backend.socket.acceptor;

import io.netty.buffer.ByteBuf;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.metadata.CompositeMetadata;
import io.rsocket.metadata.WellKnownMimeType;
import network.ranked.backend.util.Common;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author JustReddy
 */
public class SetupAuthSocketAcceptor implements SocketAcceptor {

    private final String expectedToken;
    private final RSocketMessageHandler handler;

    public SetupAuthSocketAcceptor(String expectedToken, RSocketMessageHandler handler) {
        this.expectedToken = expectedToken;
        this.handler = handler;
    }

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket rSocket) {

        CompositeMetadata metadata =
                new CompositeMetadata(setup.metadata(), false);

        String token = null;

        for (CompositeMetadata.Entry entry : metadata) {
            if (entry.getMimeType() == null) {
                continue;
            }
            if (entry.getMimeType().equals(WellKnownMimeType.TEXT_PLAIN.getString())) {
                ByteBuf buf = entry.getContent();
                token = buf.readCharSequence(buf.readableBytes(), StandardCharsets.UTF_8).toString().substring(1);
                break;
            }
        }

        if (!expectedToken.equals(token)) {
            return Mono.error(new SecurityException("Invalid setup token"));
        }

        Common.logInfo("Authenticated RSocket connection established");
        return handler.responder().accept(setup, rSocket);
    }

}
