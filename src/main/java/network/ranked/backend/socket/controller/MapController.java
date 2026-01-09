package network.ranked.backend.socket.controller;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.socket.Ack;
import network.ranked.backend.socket.packets.map.SlimeWorldMap;
import network.ranked.backend.socket.packets.requests.CreateMapRequest;
import network.ranked.backend.socket.packets.requests.DownloadMapRequest;
import network.ranked.backend.socket.parser.CustomParser;
import network.ranked.backend.socket.services.SlimeWorldMapService;
import network.ranked.backend.socket.session.SocketSession;
import network.ranked.backend.socket.store.SocketSessionStore;
import network.ranked.backend.util.Common;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author JustReddy
 */
@Controller
@RequiredArgsConstructor
@MessageMapping("map:")
public class MapController {


    private final CustomParser decoder;
    private final SocketSessionStore sessionStore;
    private final SlimeWorldMapService mapService;

    @MessageMapping("create")
    public Mono<Ack<String>> createMap(@Payload CreateMapRequest request, RSocketRequester requester) {
        Common.logInfo("SOCKET -> map:create received");
        return Mono.fromCallable(() -> {
                    final SocketSession session = sessionStore.getSession(requester.rsocketClient());

                    if (session == null || !session.isIdentified()) {
                        Common.logWarning("SOCKET -> Received CreateMap from unrecognized session");
                        return Ack.<String>error("You must identify before sending map creation requests");
                    }

                    if (mapService.getByNameAndType(request.getName(), request.getGameType()).isPresent()) {
                        Common.logWarning("SOCKET -> Method: createMap requests creating a map, but map already exists. Name: " + request.getName() + ", Type: " + request.getGameType());
                        return Ack.<String>error("A map with that name and type already exists");
                    }

                    final SlimeWorldMap map = mapService.createMap(request.getName(), request.getGameType(), request.getBuffer());

                    return Ack.ok("Map: " + map.getName() + " of type " + map.getGameType() + " created successfully");

                }).subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(Ack.error("An error occurred while creating the map: " + e.getMessage()));
                });
    }

    @MessageMapping("download")
    public Mono<Ack<byte[]>> downloadMap(@Payload DownloadMapRequest request, RSocketRequester requester) {
        Common.logInfo("SOCKET -> map:download received");
        return Mono.fromCallable(() -> {
                    final SocketSession session = sessionStore.getSession(requester.rsocketClient());

                    if (session == null || !session.isIdentified()) {
                        Common.logWarning("SOCKET -> Received CreateMap from unrecognized session");
                        return Ack.<byte[]>error("You must identify before sending map download requests");
                    }

                    final SlimeWorldMap map = mapService.getByNameAndType(request.getName(), request.getGameType())
                            .orElse(null);
                    if (map == null) {
                        Common.logWarning("SOCKET -> Method: downloadMap requests downloading a map, but the map doesn't exist. Name: " + request.getName() + ", Type: " + request.getGameType());
                        return Ack.<byte[]>error("A map with that name and type doesn't exist");
                    }

                    byte[] buffer = mapService.downloadWorld(map.getGridFsId());
                    return Ack.ok(buffer);
                }).subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(Ack.error("An error occurred while downloading a map: " + e.getMessage()));
                });
    }

}
