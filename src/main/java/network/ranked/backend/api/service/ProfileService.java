package network.ranked.backend.api.service;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.repositories.ProfileRepository;
import network.ranked.backend.socket.packets.player.profile.PlayerProfile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author JustReddy
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public Optional<PlayerProfile> getProfileByUniqueId(String uniqueId) {
        return profileRepository.findByUniqueId(UUID.fromString(uniqueId));
    }

    public Optional<PlayerProfile> getProfileByName(String name) {
        return profileRepository.findByCachedNameIgnoreCase(name);
    }

}
