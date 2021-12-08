package kz.mtszn.models.redisrepository;

import kz.mtszn.models.redis.AuthorizeUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizeUserRepository extends CrudRepository<AuthorizeUser, String> {
    Optional<AuthorizeUser> findByAccessToken(final String token);
}
