package network.ranked.backend.socket.controller;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.redis.RedisPlayerRepository;
import network.ranked.backend.redis.RedisServerRepository;
import network.ranked.backend.repositories.ProfileRepository;
import network.ranked.backend.socket.Ack;
import network.ranked.backend.socket.parser.CustomParser;
import network.ranked.backend.socket.packets.connection.PlayerConnectionUpdate;
import network.ranked.backend.socket.packets.connection.PlayerConnectionUpdateResult;
import network.ranked.backend.socket.packets.identity.ClientInfo;
import network.ranked.backend.socket.packets.identity.ClientPlayer;
import network.ranked.backend.socket.packets.player.profile.PlayerProfile;
import network.ranked.backend.socket.session.SocketSession;
import network.ranked.backend.socket.store.SocketSessionStore;
import network.ranked.backend.util.Common;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author JustReddy
 */
@Controller
@RequiredArgsConstructor
public class PlayerController {

    private final CustomParser decoder;
    private final SocketSessionStore sessionStore;
    private final RedisServerRepository serverRepository;
    private final RedisPlayerRepository playerRepository;
    private final ProfileRepository profileRepository;
    private final SocketSessionStore socketSessionStore;

    @MessageMapping("player:connect")
    public Mono<Ack<PlayerConnectionUpdateResult>> onPlayerConnect(boolean isDisconnect, PlayerConnectionUpdate connectionUpdate, RSocketRequester requester) {
        Common.logInfo("SOCKET -> player:connect received");

        return Mono.fromCallable(() -> {

            final SocketSession session = sessionStore.getSession(requester.rsocketClient());

            if (session == null || !session.isIdentified()) {
                Common.logWarning("SOCKET -> Received PlayerConnect from unrecognized session");
                return Ack.<PlayerConnectionUpdateResult>error("You must identify before sending player connection updates");
            }

            final ClientPlayer clientPlayer = connectionUpdate.getClientPlayer();

            final ClientInfo server = serverRepository.getServer(clientPlayer.serverIdentifier());
            final ClientInfo proxy = serverRepository.getServer(clientPlayer.proxyIdentifier());
            Optional<PlayerProfile> profileOpt = profileRepository.findByUniqueId(UUID.fromString(clientPlayer.uuid()));

            if (clientPlayer.serverIdentifier() != null && server == null) {
                Common.logWarning(String.format("SOCKET -> Received PlayerConnect from unrecognized server: %s", clientPlayer.serverIdentifier()));
                return Ack.ok(new PlayerConnectionUpdateResult(PlayerConnectionUpdateResult.Status.ERROR, String.format("Server %s not found.", clientPlayer.serverIdentifier())));
            }

            if (proxy == null && !isDisconnect) {
                Common.logWarning(String.format("SOCKET -> Received PlayerConnect from unrecognized proxy: %s", clientPlayer.proxyIdentifier()));
                return Ack.ok(new PlayerConnectionUpdateResult(PlayerConnectionUpdateResult.Status.ERROR, String.format("Proxy %s not found.", clientPlayer.proxyIdentifier())));
            }

            ClientInfo oldServer = null;
            if (!connectionUpdate.isAuth()) {
                if (isDisconnect) {
                    if (proxy != null) {
                        if (server != null) {
                            server.getOnlinePlayers().removeIf(player -> player.equals(clientPlayer));
                        }

                        proxy.getOnlinePlayers().removeIf(player -> player.equals(clientPlayer));
                        playerRepository.deletePlayers(List.of(clientPlayer));
                    }

                    if (profileOpt.isPresent()) {
                        PlayerProfile profile = profileOpt.get();
                        // TODO UPDATE STUFF
                    }
                } else {
                    final ClientPlayer cachedPlayer = playerRepository.getPlayers(List.of(new RedisPlayerRepository.SearchEntry(RedisPlayerRepository.SearchEntry.SearchType.UUID, clientPlayer.uuid()))).getFirst();
                    if (cachedPlayer != null && server != null &&
                            !cachedPlayer.serverIdentifier().equalsIgnoreCase(server.getIdentifier().toString())) {
                        oldServer = serverRepository.getServer(cachedPlayer.serverIdentifier());
                        if (oldServer != null) {
                            oldServer.getOnlinePlayers().removeIf(player -> player.equals(clientPlayer));
                        }
                    }

                    if (server != null && !server.getOnlinePlayers().contains(clientPlayer)) {
                        server.getOnlinePlayers().add(clientPlayer);
                    }

                    if (!proxy.getOnlinePlayers().contains(clientPlayer)) {
                        proxy.getOnlinePlayers().add(clientPlayer);
                    }
                }

                if (server != null) {
                    serverRepository.saveServer(server);
                }
                if (proxy != null) {
                    serverRepository.saveServer(proxy);
                }
                serverRepository.saveServer(oldServer);
                playerRepository.savePlayers(List.of(clientPlayer), server != null ? server.getIdentifier().toString() : null,
                        proxy == null ? null : proxy.getIdentifier().toString());

                if (server != null) {
                    socketSessionStore.broadcast(List.of(
                                    server.getIdentifier().toString(),
                                    "servers-PROXY"
                            ),
                            "client:info",
                            decoder.encode(server)
                    );
                }

                if (proxy != null) {
                    socketSessionStore.broadcast(List.of(
                                    "servers-PROXY"
                            ),
                            "client:info",
                            decoder.encode(proxy)
                    );
                }

                if (oldServer != null) {
                    socketSessionStore.broadcast(List.of(
                                    oldServer.getIdentifier().toString(),
                                    "servers-PROXY"
                            ),
                            "client:info",
                            decoder.encode(oldServer)
                    );
                }
            }


            return Ack.ok(new PlayerConnectionUpdateResult(PlayerConnectionUpdateResult.Status.SUCCESS));
        }).subscribeOn(Schedulers.boundedElastic()).onErrorResume(e -> {
            e.printStackTrace();
            return Mono.just(Ack.error("An error occurred while processing player connection " + e.getMessage()));
        });
    }
}
