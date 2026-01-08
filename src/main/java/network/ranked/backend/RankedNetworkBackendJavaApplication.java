package network.ranked.backend;

import network.ranked.backend.repositories.ProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

@EnableScheduling
@SpringBootApplication
public class RankedNetworkBackendJavaApplication implements CommandLineRunner {

    private ProfileRepository repository;

    public RankedNetworkBackendJavaApplication(ProfileRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(RankedNetworkBackendJavaApplication.class, args);

    }

    @Override
    public void run(String... args) {
    }
}
