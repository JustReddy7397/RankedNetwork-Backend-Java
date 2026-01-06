package network.ranked.backend.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import network.ranked.backend.redis.data.PlayerRedisData;
import network.ranked.backend.socket.decoder.CustomDecoder;
import network.ranked.backend.socket.packets.identity.ClientPlayer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
@Repository
@RequiredArgsConstructor
@Getter
public class RedisPlayerRepository {

    private final StringRedisTemplate redis;
    private final CustomDecoder decoder;

    public void savePlayers(List<ClientPlayer> players, String serverIdentifier, String proxyIdentifier) {
        for (final ClientPlayer clientPlayer : players) {

            final String finalServerIdentifier = serverIdentifier != null ? serverIdentifier : clientPlayer.serverIdentifier();
            final String finalProxyIdentifier = proxyIdentifier != null ? proxyIdentifier : clientPlayer.proxyIdentifier();

            final PlayerRedisData data = new PlayerRedisData(
                    clientPlayer.uuid(),
                    clientPlayer.cachedName(),
                    finalServerIdentifier,
                    finalProxyIdentifier
            );

            final String key = String.format("player:%s:%s",
                    clientPlayer.uuid(),
                    clientPlayer.cachedName().toLowerCase()
            );

            final String json = decoder.encode(data);
            redis.opsForValue().set("player:" + clientPlayer.uuid(), json);
        }
    }

    public void savePlayers(List<ClientPlayer> players, String serverIdentifier) {
        savePlayers(players, serverIdentifier, null);
    }

    public void savePlayers2(List<ClientPlayer> players, String proxyIdentifier) {
        savePlayers(players, null, proxyIdentifier);
    }

    public void savePlayers(List<ClientPlayer> players) {
        savePlayers(players, null, null);
    }

    public List<ClientPlayer> getPlayers(List<SearchEntry> searchEntries) {
        final List<String> allKeys = new ArrayList<>();

        for (final SearchEntry entry : searchEntries) {
            final String key = switch (entry.getType()) {
                case UUID -> "player:" + entry.getValue() + ":*";
                case NAME -> "player:*:" + entry.getValue().toLowerCase();
            };
            final List<String> keys = new ArrayList<>(redis.keys(key));
            allKeys.addAll(keys);
        }

        final List<ClientPlayer> players = new ArrayList<>();

        for (final String key : allKeys) {
            final String payload = redis.opsForValue().get(key);
            if (payload == null) {
                continue;
            }

            try {
                final ClientPlayer clientPlayer = decoder.decode(payload, ClientPlayer.class);
                if (clientPlayer == null) {
                    continue;
                }
                players.add(clientPlayer);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        return players;
    }

    public void deletePlayers(List<ClientPlayer> clientPlayers) {
        List<String> allKeys = new ArrayList<>();

        for (final ClientPlayer clientPlayer : clientPlayers) {
            allKeys.addAll(redis.keys(String.format("player:%s:*", clientPlayer.uuid())));
        }

        for (String key : allKeys) {
            redis.delete(key);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class SearchEntry {

        private final SearchType type;
        private final String value;

        public enum SearchType {
            UUID,
            NAME
        }
    }

}
