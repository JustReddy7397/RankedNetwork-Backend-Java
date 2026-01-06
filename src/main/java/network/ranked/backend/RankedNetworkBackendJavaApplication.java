package network.ranked.backend;

import network.ranked.backend.repositories.ProfileRepository;
import network.ranked.backend.socket.packets.player.PlayerProfile;
import network.ranked.backend.socket.packets.player.enums.PlayerStatus;
import network.ranked.backend.util.Common;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Example;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class RankedNetworkBackendJavaApplication {


    public static void main(String[] args) {
        SpringApplication.run(RankedNetworkBackendJavaApplication.class, args);

    }
}
