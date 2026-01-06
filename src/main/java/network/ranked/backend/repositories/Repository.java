package network.ranked.backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author JustReddy
 */
@NoRepositoryBean
public interface Repository<T> extends MongoRepository<T, String> {}
