package network.ranked.backend.redis;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.socket.packets.party.PartyInvite;
import network.ranked.backend.socket.parser.CustomParser;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author JustReddy
 */
@Repository
@RequiredArgsConstructor
public class RedisPartyInviteRepository {

    private final StringRedisTemplate redis;
    private final CustomParser decoder;

    private String getInviteKey(String inviterUniqueId, String invitedUniqueId) {
        return "party-invite:" + inviterUniqueId + ":" + invitedUniqueId;
    }

    public void createPartyInvite(PartyInvite invite) {
        final String key = getInviteKey(invite.getInviter().getUniqueId().toString(), invite.getInvited().getUniqueId().toString());
        final String json = decoder.encode(invite);
        redis.opsForValue().set(key, json);
    }

    public Optional<PartyInvite> getPartyInvite(UUID inviterUniqueId, UUID invitedUniqueId) {
        final String key = getInviteKey(inviterUniqueId.toString(), invitedUniqueId.toString());
        String data = redis.opsForValue().get(key);
        return data == null ? Optional.empty() : Optional.of(decoder.decode(data, PartyInvite.class));
    }

    public void deletePartyInvite(UUID inviterUniqueId, UUID invitedUniqueId) {
        final String key = getInviteKey(inviterUniqueId.toString(), invitedUniqueId.toString());
        redis.delete(key);
    }

    public List<PartyInvite> getPartyInvites() {
        String key = "party-invite:*";
        Set<String> keys = redis.keys(key);
        if (keys == null || keys.isEmpty()) {
            return List.of();
        }

        return keys.stream()
                .map(k -> redis.opsForValue().get(k))
                .filter(Objects::nonNull)
                .map(k -> decoder.decode(k, PartyInvite.class))
                .toList();
    }

}
