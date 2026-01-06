package network.ranked.backend.socket.store;

import io.rsocket.core.RSocketClient;
import network.ranked.backend.socket.session.SocketSession;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JustReddy
 */
@Component
public class SocketSessionStore {

    private final Map<String, Set<String>> rooms = new ConcurrentHashMap<>();
    private final Map<String, RSocketRequester> byIdentifier = new ConcurrentHashMap<>();
    private final Map<RSocketClient, SocketSession> sessions = new ConcurrentHashMap<>();

    public void createSession(RSocketClient client) {
        sessions.put(client, new SocketSession());
    }

    public SocketSession getSession(RSocketClient client) {
        return sessions.get(client);
    }

    public void removeSession(RSocketClient client) {
        sessions.remove(client);
    }

    public void register(String identifier, Set<String> roomsToJoin, RSocketRequester requester) {
        byIdentifier.put(identifier, requester);
        for (String room : roomsToJoin) {
            rooms.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(identifier);
        }

        requester.rsocket().onClose()
                .doFinally(signal -> disconnect(identifier))
                .subscribe();
    }

    public void disconnect(String identifier) {
        RSocketRequester requester = byIdentifier.remove(identifier);
        if (requester != null && requester.rsocket() != null) {
            requester.rsocket().dispose();
        }

        rooms.values().forEach(set -> set.remove(identifier));
    }

    public void broadcast(String room, String route, String payload) {
        Set<String> members = rooms.get(room);
        if (members == null) {
            return;
        }

        for (String memberId : members) {
            RSocketRequester requester = byIdentifier.get(memberId);
            if (requester != null) {
                requester.route(route)
                        .data(payload)
                        .send()
                        .doOnError(throwable -> disconnect(room))
                        .subscribe();
            }
        }
    }

    public void broadcast(List<String> rooms, String route, String payload) {
        for (String room : rooms) {
            broadcast(room, route, payload);
        }
    }

    public void broadcast(String route, String payload) {
        for (String room : rooms.keySet()) {
            broadcast(room, route, payload);
        }
    }

    public void send(RSocketRequester requester, String route, String payload) {
        requester.route(route).data(payload).send().subscribe();
    }

    public boolean hasRoom(String room) {
        return rooms.containsKey(room);
    }

}
