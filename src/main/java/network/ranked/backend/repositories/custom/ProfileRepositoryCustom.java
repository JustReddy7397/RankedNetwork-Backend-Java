package network.ranked.backend.repositories.custom;

import network.ranked.backend.socket.packets.player.PlayerProfile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author JustReddy
 */
public interface ProfileRepositoryCustom {

    @Transactional
    void updateProfile(UUID uniqueId, Map<String, Object> fields);

    @Transactional
    void updateProfiles(List<PlayerProfile> profiles, boolean isQuit);

    @Transactional
    void updatePartialProfiles(List<UUID> uniqueIds, Map<String, Object> fields);

}
