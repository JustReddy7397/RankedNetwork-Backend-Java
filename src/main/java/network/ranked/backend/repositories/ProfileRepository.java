package network.ranked.backend.repositories;

import io.lettuce.core.dynamic.annotation.Command;
import network.ranked.backend.repositories.custom.ProfileRepositoryCustom;
import network.ranked.backend.socket.packets.player.PlayerProfile;
import network.ranked.backend.socket.packets.player.enums.PlayerStatus;
import network.ranked.backend.util.Common;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author JustReddy
 */
public interface ProfileRepository extends Repository<PlayerProfile>, ProfileRepositoryCustom {

    Optional<PlayerProfile> findByUniqueId(UUID uniqueId);

    Optional<PlayerProfile> findByCachedName(String cachedName);

    Optional<PlayerProfile> findByDiscordId(String discordId);

    boolean existsByUniqueId(UUID uniqueId);

    boolean existsByCachedName(String cachedName);

    boolean existsByDiscordId(String discordId);

    List<PlayerProfile> findAllByUniqueIdIn(List<UUID> uniqueIds);

    List<PlayerProfile> findAllByCachedNameIn(List<String> cachedNames);

    default void createProfile(UUID uniqueId, String cachedName, boolean online) {
        if (existsByUniqueId(uniqueId)) {
            return;
        }
        PlayerProfile profile = new PlayerProfile(uniqueId, cachedName, null, online ? PlayerStatus.ONLINE : PlayerStatus.OFFLINE);
        save(profile);
        Common.logInfo("MONGO -> Created profile for " + cachedName + " (" + uniqueId + ")");
    }



}
