package network.ranked.backend.api.controller;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.api.service.ProfileService;
import network.ranked.backend.socket.packets.player.profile.PlayerProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author JustReddy
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    // TODO MAKE PROPER RESPONSE CLASSES

    private final ProfileService profileService;

    @GetMapping("/name/{name}")
    public ResponseEntity<PlayerProfile> getProfileByName(@PathVariable String name) {
        return profileService.getProfileByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<PlayerProfile> getProfileByUniqueId(@PathVariable String uuid) {
        return profileService.getProfileByUniqueId(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
