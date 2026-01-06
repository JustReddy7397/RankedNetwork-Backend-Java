package network.ranked.backend.socket.controller;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.redis.RedisServerRepository;
import network.ranked.backend.socket.Ack;
import network.ranked.backend.socket.session.SocketSession;
import network.ranked.backend.socket.decoder.CustomDecoder;
import network.ranked.backend.socket.packets.identity.ClientIdentity;
import network.ranked.backend.socket.packets.identity.ClientInfo;
import network.ranked.backend.socket.packets.identity.enums.ClientType;
import network.ranked.backend.socket.services.IdentityService;
import network.ranked.backend.socket.store.SocketSessionStore;
import network.ranked.backend.util.Common;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * @author JustReddy
 */
@Controller
@RequiredArgsConstructor
public class ClientController {

    private final CustomDecoder decoder;
    private final SocketSessionStore sessionStore;
    private final RedisServerRepository serverRepository;
    private final IdentityService identityService;

    @MessageMapping("server:identity")
    public Mono<Ack<ClientInfo>> onIdentity(
            @Payload ClientIdentity identity,
            RSocketRequester requester
    ) {
        Common.logInfo("SOCKET -> server:identity received");
        return Mono.fromCallable(() -> {
            final SocketSession session = sessionStore.getSession(requester.rsocketClient());

            if (session.isIdentified()) {
                return Ack.<ClientInfo>error(String.format("%s is already identified", session.getClientInfo().getDisplayName()));
            }

            Common.logInfo(identity.toString());
            final ClientInfo clientInfo = identityService.handle(identity);
            session.setClientInfo(clientInfo);

            sessionStore.register(clientInfo.getIdentifier().toString(),
                    Set.of("servers-" + clientInfo.getType(),
                            clientInfo.getIdentifier().toString()),
                    requester);

            if (clientInfo.getType() == ClientType.PROXY) {
                sessionStore.send(requester, "server:register",
                        decoder.encode(identityService.getAllExceptProxy())
                );
            } else {
                sessionStore.broadcast("servers-PROXY", "server:register", decoder.encode(clientInfo));
            }

            Common.logInfo("Identity registered successfully");
            return Ack.ok(clientInfo);
        }).onErrorResume(e -> {
            e.printStackTrace();
            return Mono.just(Ack.error(e.getMessage()));
        });
    }

    @MessageMapping("heartbeat")
    public Mono<Void> onHeartBeat(RSocketRequester requester) {
        Common.logInfo("a");
        final SocketSession session = sessionStore.getSession(requester.rsocketClient());

        if (session == null || !session.isIdentified()) {
            Common.logWarning("Received heartbeat from unrecognized session");
            return Mono.empty();
        }

        serverRepository.refreshTtl(session.getClientInfo().getIdentifier().toString());
        return Mono.empty();
    }


}
