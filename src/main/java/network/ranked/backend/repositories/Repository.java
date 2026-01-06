package network.ranked.backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author JustReddy
 */
public interface Repository<T> extends MongoRepository<T, String> {}
